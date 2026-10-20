package com.medioka.guideline.app.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

/** One day in the 7-day forecast. */
data class DailyWeather(
    val date: LocalDate = LocalDate(1970, 1, 1),
    val condition: WeatherCondition = WeatherCondition.CLEAR,
    val maxTemperatureCelsius: Double = 0.0,
    val minTemperatureCelsius: Double = 0.0,
    val precipitationProbabilityPercent: Int = 0,
    val precipitationMm: Double = 0.0,
    val humidityPercent: Int = 0,
    val windSpeedKmh: Double = 0.0,
    val windDirectionDegrees: Int = 0,
    val pressureHpa: Double = 0.0,
    val uvIndex: Double = 0.0,
    val sunrise: LocalDateTime = LocalDateTime(1970, 1, 1, 0, 0),
    val sunset: LocalDateTime = LocalDateTime(1970, 1, 1, 0, 0),
    val hourly: List<HourlyWeather> = emptyList(),
)
