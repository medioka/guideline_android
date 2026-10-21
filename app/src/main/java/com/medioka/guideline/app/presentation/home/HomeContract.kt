package com.medioka.guideline.app.presentation.home

import androidx.compose.runtime.Immutable
import com.medioka.guideline.app.presentation.common.location.LocationResult
import com.medioka.guideline.app.presentation.common.model.DailyForecast
import com.medioka.guideline.app.presentation.common.model.CurrentWeather
import com.medioka.guideline.app.presentation.common.model.WeatherErrorReason

/** Immutable state rendered by the Home destination. */
@Immutable
data class HomeState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val currentWeather: CurrentWeather? = null,
    val forecast: List<DailyForecast> = emptyList(),
    val error: WeatherErrorReason? = null,
)

/** User and platform events handled by [HomeViewModel]. */
sealed interface HomeIntent {
    data object ScreenStarted : HomeIntent
    data object Retry : HomeIntent
    data object Refresh : HomeIntent
    data object OpenSettings : HomeIntent
    data class ForecastSelected(val forecast: DailyForecast) : HomeIntent
    data object LocationPermissionGranted : HomeIntent
    data object LocationPermissionDenied : HomeIntent
    data class LocationResolved(val result: LocationResult) : HomeIntent
}

/** One-shot work that the UI performs using Android APIs. */
sealed interface HomeEffect {
    data object RequestLocationPermission : HomeEffect
    data object OpenAppSettings : HomeEffect
    data class ShowRefreshError(val reason: WeatherErrorReason) : HomeEffect
    data class NavigateToWeatherDay(
        val forecast: DailyForecast,
        val locationName: String,
    ) : HomeEffect
}
