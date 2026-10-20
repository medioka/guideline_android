package com.medioka.guideline.app.domain.payload

/**
 * Weather at the moment the forecast was requested.
 *
 * Values use metric units because that is what the Open-Meteo request asks for.
 * Formatting (km vs m, cardinal wind directions, labels) is a presentation
 * concern, not a domain concern.
 */
data class WeatherPayload(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)
