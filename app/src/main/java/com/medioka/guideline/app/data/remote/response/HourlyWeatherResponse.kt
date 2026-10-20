package com.medioka.guideline.app.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyWeatherResponse(
    val time: List<String>,
    @SerialName("temperature_2m") val temperatures: List<Double?>,
    @SerialName("relative_humidity_2m") val humidities: List<Int?>,
    @SerialName("pressure_msl") val pressures: List<Double?>,
    @SerialName("precipitation_probability") val precipitationProbabilities: List<Int?>,
    @SerialName("weather_code") val weatherCodes: List<Int?>,
    @SerialName("is_day") val isDay: List<Int?>,
)
