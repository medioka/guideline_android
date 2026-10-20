package com.medioka.guideline.app.presentation.model

/**
 * Minimal, UI-facing representation of the Open-Meteo weather codes.
 * The data layer is responsible for mapping WMO codes into these values.
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

data class CurrentWeather(
    val locationName: String,
    val condition: WeatherCondition,
    val temperatureCelsius: Int,
    val feelsLikeCelsius: Int,
    val highCelsius: Int,
    val lowCelsius: Int,
    val humidityPercent: Int,
    val windSpeedKmh: Int,
    val windDirection: String,
    val pressureHpa: Int,
    val uvIndex: Int,
    val visibilityKm: Double,
    val precipitationMm: Double,
    val isDay: Boolean,
    val updatedAtLabel: String,
    val sunriseLabel: String,
    val sunsetLabel: String,
)

data class HourlyForecast(
    val timeLabel: String,
    val condition: WeatherCondition,
    val temperatureCelsius: Int,
    val precipitationProbabilityPercent: Int,
    val isDay: Boolean = true,
)

data class DailyForecast(
    val dayLabel: String,
    val dateLabel: String,
    val condition: WeatherCondition,
    val highCelsius: Int,
    val lowCelsius: Int,
    val precipitationProbabilityPercent: Int,
    val precipitationMm: Double,
    val humidityPercent: Int,
    val windSpeedKmh: Int,
    val windDirection: String,
    val pressureHpa: Int,
    val uvIndex: Int,
    val sunriseLabel: String,
    val sunsetLabel: String,
    val hourly: List<HourlyForecast>,
)
