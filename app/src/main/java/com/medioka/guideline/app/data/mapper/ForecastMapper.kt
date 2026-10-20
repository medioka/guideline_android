package com.medioka.guideline.app.data.mapper

import com.medioka.guideline.app.data.remote.response.DailyWeatherResponse
import com.medioka.guideline.app.data.remote.response.ForecastResponse
import com.medioka.guideline.app.data.remote.response.HourlyWeatherResponse
import com.medioka.guideline.app.domain.model.CurrentWeather
import com.medioka.guideline.app.domain.model.DailyWeather
import com.medioka.guideline.app.domain.model.HourlyWeather
import com.medioka.guideline.app.domain.model.Weather
import com.medioka.guideline.app.domain.model.WeatherCondition
import com.medioka.guideline.app.domain.model.WeatherLocation
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.math.roundToInt

/**
 * Maps the Open-Meteo response into the domain aggregate.
 *
 * Daily humidity and pressure are not requested as daily variables, so they are
 * averaged from the hourly series. This keeps the API request small and shows
 * that mapping can do real work instead of just renaming fields.
 */
internal fun ForecastResponse.toDomain(location: WeatherLocation): Weather {
    val hourlySamples = hourly.toSamples()
    val hourlyByDate = hourlySamples.groupBy { it.time.date }
    val dailyWeather = daily.toDomain(hourlyByDate)

    val currentTime = current.time.toLocalDateTimeOrNull()
    val today = dailyWeather.firstOrNull { it.date == currentTime?.date }
        ?: dailyWeather.firstOrNull()

    val observedAt = currentTime
        ?: today?.sunrise
        ?: LocalDateTime(1970, 1, 1, 0, 0)

    return Weather(
        location = location,
        timeZone = timezone,
        current = CurrentWeather(
            condition = current.weatherCode.toWeatherCondition(),
            temperatureCelsius = current.temperatureCelsius,
            feelsLikeCelsius = current.feelsLikeCelsius,
            highCelsius = today?.maxTemperatureCelsius ?: current.temperatureCelsius,
            lowCelsius = today?.minTemperatureCelsius ?: current.temperatureCelsius,
            humidityPercent = current.humidityPercent,
            windSpeedKmh = current.windSpeedKmh,
            windDirectionDegrees = current.windDirectionDegrees,
            pressureHpa = current.pressureHpa,
            uvIndex = today?.uvIndex ?: 0.0,
            visibilityMeters = current.visibility,
            precipitationMm = current.precipitation,
            isDay = current.isDay != 0,
            observedAt = observedAt,
            sunrise = today?.sunrise ?: observedAt,
            sunset = today?.sunset ?: observedAt,
        ),
        daily = dailyWeather,
    )
}

/**
 * Open-Meteo uses WMO weather codes. Several codes map to the same app
 * condition, so the mapping stays explicit and testable.
 */
internal fun Int?.toWeatherCondition(): WeatherCondition = when (this) {
    0 -> WeatherCondition.CLEAR
    1, 2 -> WeatherCondition.PARTLY_CLOUDY
    3 -> WeatherCondition.CLOUDY
    45, 48 -> WeatherCondition.FOG
    51, 53, 55, 56, 57 -> WeatherCondition.DRIZZLE
    61, 63, 65, 66, 67, 80, 81, 82 -> WeatherCondition.RAIN
    71, 73, 75, 77, 85, 86 -> WeatherCondition.SNOW
    95, 96, 99 -> WeatherCondition.THUNDERSTORM
    else -> WeatherCondition.CLOUDY
}

private fun DailyWeatherResponse.toDomain(
    hourlyByDate: Map<LocalDate, List<HourlySample>>,
): List<DailyWeather> = time.mapIndexedNotNull { index, dateValue ->
    val date = dateValue.toLocalDateOrNull() ?: return@mapIndexedNotNull null
    val maxTemperature = maxTemperatures.getOrNull(index) ?: return@mapIndexedNotNull null
    val minTemperature = minTemperatures.getOrNull(index) ?: return@mapIndexedNotNull null
    val sunriseTime = sunrise.getOrNull(index)?.toLocalDateTimeOrNull()
        ?: return@mapIndexedNotNull null
    val sunsetTime = sunset.getOrNull(index)?.toLocalDateTimeOrNull()
        ?: return@mapIndexedNotNull null
    val samples = hourlyByDate[date].orEmpty()

    DailyWeather(
        date = date,
        condition = weatherCodes.getOrNull(index).toWeatherCondition(),
        maxTemperatureCelsius = maxTemperature,
        minTemperatureCelsius = minTemperature,
        precipitationProbabilityPercent = precipitationProbabilityMax.getOrNull(index) ?: 0,
        precipitationMm = precipitationSum.getOrNull(index) ?: 0.0,
        humidityPercent = samples
            .map { it.humidityPercent }
            .averageHumidityOrZero()
            .roundToInt(),
        windSpeedKmh = windSpeedMax.getOrNull(index) ?: 0.0,
        windDirectionDegrees = windDirectionDominant.getOrNull(index) ?: 0,
        pressureHpa = samples
            .map { it.pressureHpa }
            .averagePressureOrZero(),
        uvIndex = uvIndexMax.getOrNull(index) ?: 0.0,
        sunrise = sunriseTime,
        sunset = sunsetTime,
        hourly = samples.map { it.toDomain() },
    )
}

private fun HourlyWeatherResponse.toSamples(): List<HourlySample> =
    time.mapIndexedNotNull { index, timeValue ->
        val parsedTime = timeValue.toLocalDateTimeOrNull() ?: return@mapIndexedNotNull null
        val temperature = temperatures.getOrNull(index) ?: return@mapIndexedNotNull null

        HourlySample(
            time = parsedTime,
            condition = weatherCodes.getOrNull(index).toWeatherCondition(),
            temperatureCelsius = temperature,
            humidityPercent = humidities.getOrNull(index) ?: 0,
            pressureHpa = pressures.getOrNull(index) ?: 0.0,
            precipitationProbabilityPercent = precipitationProbabilities.getOrNull(index) ?: 0,
            isDay = (isDay.getOrNull(index) ?: 1) != 0,
        )
    }

private fun HourlySample.toDomain(): HourlyWeather = HourlyWeather(
    time = time,
    condition = condition,
    temperatureCelsius = temperatureCelsius,
    precipitationProbabilityPercent = precipitationProbabilityPercent,
    isDay = isDay,
)

private fun String.toLocalDateTimeOrNull(): LocalDateTime? =
    runCatching { LocalDateTime.parse(this) }.getOrNull()

private fun String.toLocalDateOrNull(): LocalDate? =
    runCatching { LocalDate.parse(this) }.getOrNull()

private fun List<Int>.averageHumidityOrZero(): Double =
    if (isEmpty()) 0.0 else average()

private fun List<Double>.averagePressureOrZero(): Double =
    if (isEmpty()) 0.0 else average()

private data class HourlySample(
    val time: LocalDateTime,
    val condition: WeatherCondition,
    val temperatureCelsius: Double,
    val humidityPercent: Int,
    val pressureHpa: Double,
    val precipitationProbabilityPercent: Int,
    val isDay: Boolean,
)
