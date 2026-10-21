package com.medioka.guideline.app.presentation.common.model

sealed interface WeatherUiState {
    data object Loading : WeatherUiState

    data class Success(
        val current: CurrentWeather,
        val forecast: List<DailyForecast>,
    ) : WeatherUiState

    data class Error(
        val reason: WeatherErrorReason
    ) : WeatherUiState
}

enum class WeatherErrorReason {
    /** The device location could not be resolved. */
    LOCATION,

    /** The forecast request failed (network, server, parsing, ...). */
    NETWORK,
}
