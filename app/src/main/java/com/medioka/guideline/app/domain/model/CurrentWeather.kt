package com.medioka.guideline.app.domain.model

import kotlinx.datetime.LocalDateTime

/**
 * Weather at the moment the forecast was requested.
 *
 * Values use metric units because that is what the Open-Meteo request asks for.
 * Formatting (km vs m, cardinal wind directions, labels) is a presentation
 * concern, not a domain concern.
 */
data class CurrentWeather(
    val condition: WeatherCondition = WeatherCondition.CLEAR,
    val temperatureCelsius: Double = 0.0,
    val feelsLikeCelsius: Double = 0.0,
    val highCelsius: Double = 0.0,
    val lowCelsius: Double = 0.0,
    val humidityPercent: Int = 0,
    val windSpeedKmh: Double = 0.0,
    val windDirectionDegrees: Int = 0,
    val pressureHpa: Double = 0.0,
    val uvIndex: Double = 0.0,
    val visibilityMeters: Double = 0.0,
    val precipitationMm: Double = 0.0,
    val isDay: Boolean = true,
    val observedAt: LocalDateTime = LocalDateTime(1970, 1, 1, 0, 0),
    val sunrise: LocalDateTime = LocalDateTime(1970, 1, 1, 0, 0),
    val sunset: LocalDateTime = LocalDateTime(1970, 1, 1, 0, 0),
)
