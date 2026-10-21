package com.medioka.guideline.app.presentation.home

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medioka.guideline.app.domain.error.WeatherException
import com.medioka.guideline.app.domain.payload.WeatherPayload
import com.medioka.guideline.app.domain.usecase.GetWeatherUseCase
import com.medioka.guideline.app.presentation.common.location.LocationHelper
import com.medioka.guideline.app.presentation.common.location.LocationResult
import com.medioka.guideline.app.presentation.common.model.WeatherErrorReason
import com.medioka.guideline.app.presentation.common.model.WeatherUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Owns the Home state and translates user callbacks into use case calls.
 *
 * Android permission and location APIs are handled by the screen. The screen
 * reports the outcome through [handleLocationResult], keeping this class free of
 * Context and platform location types.
 */
class HomeViewModel(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val locationHelper: LocationHelper
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val state: StateFlow<WeatherUiState> = _state.asStateFlow()
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _locationRequestEvent = Channel<Unit>(Channel.BUFFERED)
    val locationRequestEvent = _locationRequestEvent.receiveAsFlow()
    private val refreshErrorChannel = Channel<WeatherErrorReason>(Channel.BUFFERED)
    val refreshErrorEvents = refreshErrorChannel.receiveAsFlow()

    private var lastLocation: WeatherPayload? = null
    private var hasStarted = false
    private var loadJob: Job? = null

    fun init() {
        if (hasStarted) return
        hasStarted = true
        requestLocation()
    }

    fun onRefresh() {
        lastLocation?.let(::load) ?: requestLocation()
    }

    fun onRetry() {
        val state = _state.value
        if (state is WeatherUiState.Error && state.reason == WeatherErrorReason.LOCATION) {
            requestLocation()
        } else {
            lastLocation?.let(::load) ?: requestLocation()
        }
    }

    fun handleLocationResult(result: LocationResult) {
        when (result) {
            is LocationResult.Available -> {
                lastLocation = result.location
                load(result.location)
            }

            is LocationResult.PermissionDenied -> {
                showLocationError()
            }

            LocationResult.Unavailable -> {
                showLocationError()
            }
        }
    }

    private fun requestLocation() {
        viewModelScope.launch {
            loadJob?.cancel()
            val refreshing = _state.value is WeatherUiState.Success || _isRefreshing.value
            _isRefreshing.value = refreshing
            if (!refreshing) _state.value = WeatherUiState.Loading
            _locationRequestEvent.send(Unit)
        }
    }

    fun askPermissionAndRequestLocation(activity: Activity, onRequestPermission: () -> Unit) {
        viewModelScope.launch {
            try {
                locationHelper.requestLocation(activity, onRequestPermission)
                    ?.let(::handleLocationResult)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                handleLocationResult(LocationResult.Unavailable)
            }
        }
    }

    fun getCurrentLocation() {
        viewModelScope.launch {
            try {
                handleLocationResult(locationHelper.getCurrentLocation())
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                Log.e("HomeViewModel", "Could not get device location", error)
                handleLocationResult(LocationResult.Unavailable)
            }
        }
    }

    private fun load(payload: WeatherPayload) {
        loadJob?.cancel()
        val refreshing = _state.value is WeatherUiState.Success || _isRefreshing.value
        _isRefreshing.value = refreshing
        if (!refreshing) _state.value = WeatherUiState.Loading
        loadJob = viewModelScope.launch {
            try {
                val weather = getWeatherUseCase(payload)
                val success = weather.toUiState()
                _isRefreshing.value = false
                _state.value = success
            } catch (error: CancellationException) {
                throw error
            } catch (error: WeatherException) {
                showError(
                    when (error) {
                        is WeatherException.LocationUnavailable,
                        is WeatherException.InvalidLocation,
                        -> WeatherErrorReason.LOCATION

                        else -> WeatherErrorReason.NETWORK
                    },
                )
            }
        }
    }

    private fun showLocationError() = showError(WeatherErrorReason.LOCATION)

    private fun showError(reason: WeatherErrorReason) {
        val hasDataForRefresh = _state.value is WeatherUiState.Success && _isRefreshing.value
        _isRefreshing.value = false
        if (hasDataForRefresh) {
            refreshErrorChannel.trySend(reason)
        } else {
            _state.value = WeatherUiState.Error(reason)
        }
    }
}
