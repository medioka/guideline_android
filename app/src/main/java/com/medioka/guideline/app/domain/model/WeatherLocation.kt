package com.medioka.guideline.app.domain.model

/**
 * A place the forecast can be requested for.
 *
 * [name] is optional because coordinates from the device do not always have a
 * human-readable label. Presentation can fall back to "Current location".
 */
data class WeatherLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val name: String? = null,
)
