package com.medioka.guideline.app.presentation.home

import com.medioka.guideline.app.domain.model.CurrentWeather as DomainCurrentWeather
import com.medioka.guideline.app.domain.model.DailyWeather
import com.medioka.guideline.app.domain.model.HourlyWeather
import com.medioka.guideline.app.domain.model.Weather
import com.medioka.guideline.app.domain.model.WeatherCondition as DomainWeatherCondition
import com.medioka.guideline.app.presentation.common.model.CurrentWeather
import com.medioka.guideline.app.presentation.common.model.DailyForecast
import com.medioka.guideline.app.presentation.common.model.HourlyForecast
import com.medioka.guideline.app.presentation.common.model.WeatherCondition
import com.medioka.guideline.app.presentation.common.model.WeatherUiState
import java.util.Locale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.math.roundToInt

private const val FALLBACK_LOCATION_NAME = "Current location"

/**
 * Maps the domain aggregate into the immutable state consumed by Compose.
 *
 * Formatting belongs to presentation, so unit conversion, labels and rounded
 * display values happen here instead of leaking into the domain.
 */
internal fun Weather.toUiState(): WeatherUiState.Success {
    val today = current.observedAt.date

    return WeatherUiState.Success(
        current = current.toUi(
            locationName = location.name ?: FALLBACK_LOCATION_NAME,
        ),
        forecast = daily.map { it.toUi(today = today) },
    )
}

private fun DomainCurrentWeather.toUi(locationName: String): CurrentWeather = CurrentWeather(
    locationName = locationName,
    condition = condition.toUi(),
    temperatureCelsius = temperatureCelsius.roundToInt(),
    feelsLikeCelsius = feelsLikeCelsius.roundToInt(),
    highCelsius = highCelsius.roundToInt(),
    lowCelsius = lowCelsius.roundToInt(),
    humidityPercent = humidityPercent,
    windSpeedKmh = windSpeedKmh.roundToInt(),
    windDirection = windDirectionDegrees.toCardinalDirection(),
    pressureHpa = pressureHpa.roundToInt(),
    uvIndex = uvIndex.roundToInt(),
    visibilityKm = visibilityMeters / 1_000.0,
    precipitationMm = precipitationMm,
    isDay = isDay,
    updatedAtLabel = observedAt.toTimeLabel(),
    sunriseLabel = sunrise.toTimeLabel(),
    sunsetLabel = sunset.toTimeLabel(),
)

private fun DailyWeather.toUi(today: LocalDate): DailyForecast = DailyForecast(
    dayLabel = date.toDayLabel(today = today),
    dateLabel = date.toDateLabel(),
    condition = condition.toUi(),
    highCelsius = maxTemperatureCelsius.roundToInt(),
    lowCelsius = minTemperatureCelsius.roundToInt(),
    precipitationProbabilityPercent = precipitationProbabilityPercent,
    precipitationMm = precipitationMm,
    humidityPercent = humidityPercent,
    windSpeedKmh = windSpeedKmh.roundToInt(),
    windDirection = windDirectionDegrees.toCardinalDirection(),
    pressureHpa = pressureHpa.roundToInt(),
    uvIndex = uvIndex.roundToInt(),
    sunriseLabel = sunrise.toTimeLabel(),
    sunsetLabel = sunset.toTimeLabel(),
    hourly = hourly.map { it.toUi() },
)

private fun HourlyWeather.toUi(): HourlyForecast = HourlyForecast(
    timeLabel = time.toTimeLabel(),
    condition = condition.toUi(),
    temperatureCelsius = temperatureCelsius.roundToInt(),
    precipitationProbabilityPercent = precipitationProbabilityPercent,
    isDay = isDay,
)

private fun DomainWeatherCondition.toUi(): WeatherCondition = when (this) {
    DomainWeatherCondition.CLEAR -> WeatherCondition.CLEAR
    DomainWeatherCondition.PARTLY_CLOUDY -> WeatherCondition.PARTLY_CLOUDY
    DomainWeatherCondition.CLOUDY -> WeatherCondition.CLOUDY
    DomainWeatherCondition.FOG -> WeatherCondition.FOG
    DomainWeatherCondition.DRIZZLE -> WeatherCondition.DRIZZLE
    DomainWeatherCondition.RAIN -> WeatherCondition.RAIN
    DomainWeatherCondition.SNOW -> WeatherCondition.SNOW
    DomainWeatherCondition.THUNDERSTORM -> WeatherCondition.THUNDERSTORM
}

private fun LocalDate.toDayLabel(today: LocalDate): String =
    if (this == today) {
        "Today"
    } else {
        dayOfWeek.name.take(3).toTitleCase()
    }

private fun LocalDate.toDateLabel(): String =
    "${month.name.take(3).toTitleCase()} $dayOfMonth"

private fun LocalDateTime.toTimeLabel(): String {
    val hour = hour % 12
    val displayHour = if (hour == 0) 12 else hour
    val suffix = if (this.hour < 12) "AM" else "PM"
    val minutePart = if (minute == 0) "" else ":${minute.toString().padStart(2, '0')}"
    return "$displayHour$minutePart $suffix"
}

private fun Int.toCardinalDirection(): String {
    val normalized = ((this % 360) + 360) % 360
    val index = ((normalized + 22) / 45) % CARDINAL_DIRECTIONS.size
    return CARDINAL_DIRECTIONS[index]
}

private fun String.toTitleCase(): String =
    lowercase(Locale.ROOT).replaceFirstChar { it.titlecase(Locale.ROOT) }

private val CARDINAL_DIRECTIONS = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
