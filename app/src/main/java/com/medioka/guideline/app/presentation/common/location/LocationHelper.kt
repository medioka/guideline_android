package com.medioka.guideline.app.presentation.common.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.CancellationSignal
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.medioka.guideline.app.domain.model.WeatherLocation
import com.medioka.guideline.app.domain.payload.WeatherPayload
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.time.Duration.Companion.milliseconds

private const val LOCATION_TIMEOUT_MILLIS = 10_000L

class LocationHelper(private val appContext: Context) {

    suspend fun requestLocation(
        activity: Activity,
        onRequestPermission: () -> Unit,
    ): LocationResult? {
        if (activity.hasLocationPermission()) {
            Log.d("TEST", "GET CURRENT")
            return getCurrentLocation()
        } else {
            Log.d("TEST", "GET Request")
            onRequestPermission()
            return null
        }
    }


    suspend fun getCurrentLocation(): LocationResult {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFineLocationPermission && !hasCoarseLocationPermission) {
            return LocationResult.PermissionDenied
        }

        val locationManager = appContext.getSystemService(LocationManager::class.java)
            ?: return LocationResult.Unavailable

        // Try Network first for a quicker fix, then GPS when fine permission allows it.
        val providers = buildList {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                add(LocationManager.NETWORK_PROVIDER)
            }
            if (hasFineLocationPermission &&
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            ) {
                add(LocationManager.GPS_PROVIDER)
            }
        }

        if (providers.isEmpty()) return LocationResult.Unavailable

        var currentLocation: Location? = null
        val timeoutPerProvider = LOCATION_TIMEOUT_MILLIS / providers.size
        for (provider in providers) {
            val location = try {
                withTimeoutOrNull(timeoutPerProvider.milliseconds) {
                    locationManager.awaitCurrentLocation(provider)
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                Log.w("LocationHelper", "Current location failed for $provider", error)
                null
            }
            if (location != null) {
                currentLocation = location
                break
            }
        }

        // A current fix may be unavailable (e.g. indoors or on an emulator); use
        // the newest cached fix from any enabled provider before giving up.
        val location = currentLocation ?: providers
            .mapNotNull { provider ->
                runCatching { locationManager.getLastKnownLocation(provider) }.getOrNull()
            }
            .maxByOrNull(Location::getTime)
            ?: return LocationResult.Unavailable

        return LocationResult.Available(
            WeatherPayload(
                latitude = location.latitude,
                longitude = location.longitude,
            )
        )
    }

    private suspend fun LocationManager.awaitCurrentLocation(provider: String): Location? {
        val hasFinePermission = ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarsePermission = ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        val hasProviderPermission = if (provider == LocationManager.GPS_PROVIDER) {
            hasFinePermission
        } else {
            hasFinePermission || hasCoarsePermission
        }

        if (!hasProviderPermission) return null

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return awaitLegacyCurrentLocation(provider)
        }

        return suspendCancellableCoroutine { continuation ->
            val cancellationSignal = CancellationSignal()
            continuation.invokeOnCancellation { cancellationSignal.cancel() }
            try {
                getCurrentLocation(
                    provider,
                    cancellationSignal,
                    ContextCompat.getMainExecutor(appContext),
                ) { location ->
                    if (continuation.isActive) continuation.resume(location)
                }
            } catch (_: SecurityException) {
                // Permission could be revoked after the check above.
                if (continuation.isActive) continuation.resume(null)
            }
        }
    }

    @Suppress("DEPRECATION")
    private suspend fun LocationManager.awaitLegacyCurrentLocation(provider: String): Location? =
        suspendCancellableCoroutine { continuation ->
            lateinit var listener: LocationListener

            fun finish(location: Location?) {
                if (continuation.isActive) continuation.resume(location)
                runCatching { removeUpdates(listener) }
            }

            listener = object : LocationListener {
                override fun onLocationChanged(location: Location) = finish(location)

                override fun onProviderEnabled(provider: String) = Unit

                override fun onProviderDisabled(provider: String) = finish(null)

                override fun onStatusChanged(
                    provider: String?,
                    status: Int,
                    extras: android.os.Bundle?,
                ) = Unit
            }

            continuation.invokeOnCancellation {
                runCatching { removeUpdates(listener) }
            }

            try {
                requestLocationUpdates(
                    provider,
                    0L,
                    0f,
                    listener,
                    Looper.getMainLooper(),
                )
            } catch (error: SecurityException) {
                finish(null)
            } catch (error: Exception) {
                Log.w("LocationHelper", "Legacy location request failed for $provider", error)
                finish(null)
            }
        }
}
