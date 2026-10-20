package com.medioka.guideline.app.domain.model

/**
 * Weather conditions the app understands.
 *
 * The data layer translates provider-specific codes (for example Open-Meteo
 * WMO codes) into these values. The domain never depends on a network DTO.
 */
enum class WeatherCondition {
    CLEAR,
    PARTLY_CLOUDY,
    CLOUDY,
    FOG,
    DRIZZLE,
    RAIN,
    SNOW,
    THUNDERSTORM,
}
