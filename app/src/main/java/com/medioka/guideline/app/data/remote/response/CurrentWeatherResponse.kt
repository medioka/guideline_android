package com.medioka.guideline.app.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    val time: String,
    @SerialName("temperature_2m") val temperatureCelsius: Double,
    @SerialName("relative_humidity_2m") val humidityPercent: Int,
    @SerialName("apparent_temperature") val feelsLikeCelsius: Double,
    @SerialName("is_day") val isDay: Int,
    val precipitation: Double,
    @SerialName("weather_code") val weatherCode: Int,
    @SerialName("pressure_msl") val pressureHpa: Double,
    @SerialName("wind_speed_10m") val windSpeedKmh: Double,
    @SerialName("wind_direction_10m") val windDirectionDegrees: Int,
    val visibility: Double,
)
