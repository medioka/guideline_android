package com.medioka.guideline.app.domain

import com.medioka.guideline.app.domain.model.CurrentWeather
import com.medioka.guideline.app.domain.model.Weather
import com.medioka.guideline.app.domain.model.WeatherCondition
import com.medioka.guideline.app.domain.model.WeatherLocation
import kotlinx.datetime.LocalDateTime

internal fun testLocation(
    latitude: Double = 37.7749,
    longitude: Double = -122.4194,
    name: String? = "San Francisco",
) = WeatherLocation(latitude = latitude, longitude = longitude, name = name)

internal fun testWeather(
    location: WeatherLocation = testLocation(),
): Weather = Weather(
    location = location,
    timeZone = "America/Los_Angeles",
    current = CurrentWeather(
        condition = WeatherCondition.PARTLY_CLOUDY,
        temperatureCelsius = 18.0,
        feelsLikeCelsius = 17.0,
        highCelsius = 21.0,
        lowCelsius = 13.0,
        humidityPercent = 68,
        windSpeedKmh = 14.0,
        windDirectionDegrees = 270,
        pressureHpa = 1014.0,
        uvIndex = 4.0,
        visibilityMeters = 16_000.0,
        precipitationMm = 0.0,
        isDay = true,
        observedAt = LocalDateTime(2026, 10, 20, 9, 41),
        sunrise = LocalDateTime(2026, 10, 20, 7, 22),
        sunset = LocalDateTime(2026, 10, 20, 18, 31),
    ),
    daily = emptyList(),
)
