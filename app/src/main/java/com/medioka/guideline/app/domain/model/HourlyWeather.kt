package com.medioka.guideline.app.domain.model

import kotlinx.datetime.LocalDateTime

/** One hourly forecast entry used by the detail screen. */
data class HourlyWeather(
    val time: LocalDateTime = LocalDateTime(1970, 1, 1, 0, 0),
    val condition: WeatherCondition = WeatherCondition.CLEAR,
    val temperatureCelsius: Double = 0.0,
    val precipitationProbabilityPercent: Int = 0,
    val isDay: Boolean = true,
)
