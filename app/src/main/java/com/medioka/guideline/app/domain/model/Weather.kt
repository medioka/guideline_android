package com.medioka.guideline.app.domain.model

/**
 * Aggregate returned by [com.medioka.guideline.app.domain.repository.WeatherRepository].
 *
 * The UI needs the current conditions and the 7-day forecast together, so the
 * domain exposes them as one object instead of leaking separate API responses.
 */
data class Weather(
    val location: WeatherLocation = WeatherLocation(),
    val timeZone: String = "UTC",
    val current: CurrentWeather = CurrentWeather(),
    val daily: List<DailyWeather> = emptyList(),
)
