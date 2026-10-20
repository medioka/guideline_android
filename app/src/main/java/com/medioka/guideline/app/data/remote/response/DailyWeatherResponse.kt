package com.medioka.guideline.app.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyWeatherResponse(
    val time: List<String>,
    @SerialName("weather_code") val weatherCodes: List<Int?>,
    @SerialName("temperature_2m_max") val maxTemperatures: List<Double?>,
    @SerialName("temperature_2m_min") val minTemperatures: List<Double?>,
    val sunrise: List<String>,
    val sunset: List<String>,
    @SerialName("uv_index_max") val uvIndexMax: List<Double?>,
    @SerialName("precipitation_sum") val precipitationSum: List<Double?>,
    @SerialName("precipitation_probability_max") val precipitationProbabilityMax: List<Int?>,
    @SerialName("wind_speed_10m_max") val windSpeedMax: List<Double?>,
    @SerialName("wind_direction_10m_dominant") val windDirectionDominant: List<Int?>,
)
