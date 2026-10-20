package com.medioka.guideline.app.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Raw Open-Meteo forecast response.
 *
 * Response models are deliberately provider-shaped. They are allowed to change
 * when the API changes; the domain models are the stable contract.
 */
@Serializable
data class ForecastResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int,
    val current: CurrentWeatherResponse,
    val hourly: HourlyWeatherResponse,
    val daily: DailyWeatherResponse,
)
