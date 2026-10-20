package com.medioka.guideline.app.presentation

import com.medioka.guideline.app.presentation.model.CurrentWeather
import com.medioka.guideline.app.presentation.model.DailyForecast
import com.medioka.guideline.app.presentation.model.HourlyForecast
import com.medioka.guideline.app.presentation.model.WeatherCondition
import com.medioka.guideline.app.presentation.model.WeatherErrorReason
import com.medioka.guideline.app.presentation.model.WeatherUiState

/**
 * Placeholder content for previews and for the first UI-only run.
 * Replace with real Open-Meteo data once the data layer is wired up.
 */
object WeatherSampleData {

    val current = CurrentWeather(
        locationName = "San Francisco",
        condition = WeatherCondition.PARTLY_CLOUDY,
        temperatureCelsius = 18,
        feelsLikeCelsius = 17,
        highCelsius = 21,
        lowCelsius = 13,
        humidityPercent = 68,
        windSpeedKmh = 14,
        windDirection = "W",
        pressureHpa = 1014,
        uvIndex = 4,
        visibilityKm = 16.0,
        precipitationMm = 0.0,
        isDay = true,
        updatedAtLabel = "9:41 AM",
        sunriseLabel = "7:22 AM",
        sunsetLabel = "6:31 PM",
    )

    val forecast: List<DailyForecast> = listOf(
        DailyForecast(
            dayLabel = "Today",
            dateLabel = "Oct 20",
            condition = WeatherCondition.PARTLY_CLOUDY,
            highCelsius = 21,
            lowCelsius = 13,
            precipitationProbabilityPercent = 10,
            precipitationMm = 0.0,
            humidityPercent = 68,
            windSpeedKmh = 14,
            windDirection = "W",
            pressureHpa = 1014,
            uvIndex = 4,
            sunriseLabel = "7:22 AM",
            sunsetLabel = "6:31 PM",
            hourly = hourly(
                condition = WeatherCondition.PARTLY_CLOUDY,
                temperatures = listOf(14, 16, 20, 21, 18, 15),
                precipitation = listOf(0, 0, 10, 10, 5, 5),
            ),
        ),
        DailyForecast(
            dayLabel = "Wed",
            dateLabel = "Oct 21",
            condition = WeatherCondition.CLEAR,
            highCelsius = 24,
            lowCelsius = 14,
            precipitationProbabilityPercent = 0,
            precipitationMm = 0.0,
            humidityPercent = 55,
            windSpeedKmh = 9,
            windDirection = "NW",
            pressureHpa = 1018,
            uvIndex = 6,
            sunriseLabel = "7:23 AM",
            sunsetLabel = "6:30 PM",
            hourly = hourly(
                condition = WeatherCondition.CLEAR,
                temperatures = listOf(15, 18, 23, 24, 20, 16),
                precipitation = listOf(0, 0, 0, 0, 0, 0),
            ),
        ),
        DailyForecast(
            dayLabel = "Thu",
            dateLabel = "Oct 22",
            condition = WeatherCondition.RAIN,
            highCelsius = 17,
            lowCelsius = 12,
            precipitationProbabilityPercent = 80,
            precipitationMm = 6.4,
            humidityPercent = 82,
            windSpeedKmh = 22,
            windDirection = "SW",
            pressureHpa = 1004,
            uvIndex = 2,
            sunriseLabel = "7:24 AM",
            sunsetLabel = "6:29 PM",
            hourly = hourly(
                condition = WeatherCondition.RAIN,
                temperatures = listOf(13, 15, 17, 16, 14, 13),
                precipitation = listOf(60, 80, 85, 75, 60, 50),
            ),
        ),
        DailyForecast(
            dayLabel = "Fri",
            dateLabel = "Oct 23",
            condition = WeatherCondition.THUNDERSTORM,
            highCelsius = 16,
            lowCelsius = 11,
            precipitationProbabilityPercent = 90,
            precipitationMm = 12.8,
            humidityPercent = 88,
            windSpeedKmh = 28,
            windDirection = "W",
            pressureHpa = 998,
            uvIndex = 1,
            sunriseLabel = "7:25 AM",
            sunsetLabel = "6:27 PM",
            hourly = hourly(
                condition = WeatherCondition.THUNDERSTORM,
                temperatures = listOf(12, 14, 16, 15, 13, 12),
                precipitation = listOf(80, 90, 95, 85, 70, 60),
            ),
        ),
        DailyForecast(
            dayLabel = "Sat",
            dateLabel = "Oct 24",
            condition = WeatherCondition.CLOUDY,
            highCelsius = 19,
            lowCelsius = 12,
            precipitationProbabilityPercent = 25,
            precipitationMm = 0.4,
            humidityPercent = 74,
            windSpeedKmh = 16,
            windDirection = "W",
            pressureHpa = 1011,
            uvIndex = 3,
            sunriseLabel = "7:26 AM",
            sunsetLabel = "6:26 PM",
            hourly = hourly(
                condition = WeatherCondition.CLOUDY,
                temperatures = listOf(13, 15, 19, 18, 16, 14),
                precipitation = listOf(20, 25, 25, 20, 15, 10),
            ),
        ),
        DailyForecast(
            dayLabel = "Sun",
            dateLabel = "Oct 25",
            condition = WeatherCondition.DRIZZLE,
            highCelsius = 18,
            lowCelsius = 13,
            precipitationProbabilityPercent = 55,
            precipitationMm = 1.2,
            humidityPercent = 79,
            windSpeedKmh = 18,
            windDirection = "SW",
            pressureHpa = 1008,
            uvIndex = 2,
            sunriseLabel = "7:27 AM",
            sunsetLabel = "6:25 PM",
            hourly = hourly(
                condition = WeatherCondition.DRIZZLE,
                temperatures = listOf(14, 15, 18, 17, 15, 14),
                precipitation = listOf(40, 55, 60, 55, 45, 35),
            ),
        ),
        DailyForecast(
            dayLabel = "Mon",
            dateLabel = "Oct 26",
            condition = WeatherCondition.CLEAR,
            highCelsius = 22,
            lowCelsius = 14,
            precipitationProbabilityPercent = 5,
            precipitationMm = 0.0,
            humidityPercent = 60,
            windSpeedKmh = 11,
            windDirection = "NW",
            pressureHpa = 1016,
            uvIndex = 5,
            sunriseLabel = "7:28 AM",
            sunsetLabel = "6:24 PM",
            hourly = hourly(
                condition = WeatherCondition.CLEAR,
                temperatures = listOf(15, 18, 22, 22, 19, 16),
                precipitation = listOf(5, 5, 0, 0, 0, 0),
            ),
        ),
    )

    val success: WeatherUiState.Success = WeatherUiState.Success(
        current = current,
        forecast = forecast,
    )

    val loading: WeatherUiState = WeatherUiState.Loading

    val locationError: WeatherUiState = WeatherUiState.Error(WeatherErrorReason.LOCATION)

    val networkError: WeatherUiState = WeatherUiState.Error(WeatherErrorReason.NETWORK)
}

private fun hourly(
    condition: WeatherCondition,
    temperatures: List<Int>,
    precipitation: List<Int>,
): List<HourlyForecast> {
    val labels = listOf("6 AM", "9 AM", "12 PM", "3 PM", "6 PM", "9 PM")
    return labels.mapIndexed { index, label ->
        HourlyForecast(
            timeLabel = label,
            condition = condition,
            temperatureCelsius = temperatures[index],
            precipitationProbabilityPercent = precipitation[index],
            isDay = index < labels.lastIndex,
        )
    }
}
