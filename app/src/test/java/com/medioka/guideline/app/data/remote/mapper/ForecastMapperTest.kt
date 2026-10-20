package com.medioka.guideline.app.data.remote.mapper

import com.medioka.guideline.app.data.mapper.toDomain
import com.medioka.guideline.app.data.mapper.toWeatherCondition
import com.medioka.guideline.app.data.remote.response.ForecastResponse
import com.medioka.guideline.app.domain.model.WeatherCondition
import com.medioka.guideline.app.domain.testLocation
import com.medioka.guideline.app.testJson
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ForecastMapperTest {

    private val response: ForecastResponse = testJson.decodeFromString(
        readResource("/open_meteo_response.json"),
    )

    @Test
    fun `maps current conditions and today's daily values`() {
        val weather = response.toDomain(testLocation())

        assertEquals("America/Los_Angeles", weather.timeZone)
        assertEquals("San Francisco", weather.location.name)
        assertEquals(WeatherCondition.PARTLY_CLOUDY, weather.current.condition)
        assertEquals(12.0, weather.current.temperatureCelsius, 0.001)
        assertEquals(11.0, weather.current.feelsLikeCelsius, 0.001)
        assertEquals(21.0, weather.current.highCelsius, 0.001)
        assertEquals(13.0, weather.current.lowCelsius, 0.001)
        assertEquals(80, weather.current.humidityPercent)
        assertEquals(14.0, weather.current.windSpeedKmh, 0.001)
        assertEquals(270, weather.current.windDirectionDegrees)
        assertEquals(1010.0, weather.current.pressureHpa, 0.001)
        assertEquals(4.0, weather.current.uvIndex, 0.001)
        assertEquals(10_000.0, weather.current.visibilityMeters, 0.001)
        assertFalse(weather.current.isDay)
        assertEquals(LocalDateTime(2026, 10, 20, 0, 0), weather.current.observedAt)
        assertEquals(LocalDateTime(2026, 10, 20, 7, 22), weather.current.sunrise)
        assertEquals(LocalDateTime(2026, 10, 20, 18, 31), weather.current.sunset)
    }

    @Test
    fun `maps daily forecast and aggregates humidity and pressure from hourly`() {
        val weather = response.toDomain(testLocation())

        assertEquals(2, weather.daily.size)

        val first = weather.daily[0]
        assertEquals(WeatherCondition.PARTLY_CLOUDY, first.condition)
        assertEquals(21.0, first.maxTemperatureCelsius, 0.001)
        assertEquals(13.0, first.minTemperatureCelsius, 0.001)
        assertEquals(10, first.precipitationProbabilityPercent)
        assertEquals(0.0, first.precipitationMm, 0.001)
        assertEquals(82, first.humidityPercent)
        assertEquals(1011.0, first.pressureHpa, 0.001)
        assertEquals(4.0, first.uvIndex, 0.001)
        assertEquals(3, first.hourly.size)

        val second = weather.daily[1]
        assertEquals(WeatherCondition.RAIN, second.condition)
        assertEquals(80, second.precipitationProbabilityPercent)
        assertEquals(6.4, second.precipitationMm, 0.001)
        assertEquals(70, second.humidityPercent)
        assertEquals(1015.0, second.pressureHpa, 0.001)
        assertEquals(225, second.windDirectionDegrees)
        assertEquals(1, second.hourly.size)
    }

    @Test
    fun `maps hourly entries including day and night flags`() {
        val hourly = response.toDomain(testLocation()).daily[0].hourly

        assertEquals(LocalDateTime(2026, 10, 20, 0, 0), hourly[0].time)
        assertEquals(12.0, hourly[0].temperatureCelsius, 0.001)
        assertEquals(WeatherCondition.CLEAR, hourly[0].condition)
        assertFalse(hourly[0].isDay)
        assertTrue(hourly[2].isDay)
        assertEquals(20, hourly[2].precipitationProbabilityPercent)
    }

    @Test
    fun `maps WMO weather codes to domain conditions`() {
        assertEquals(WeatherCondition.CLEAR, 0.toWeatherCondition())
        assertEquals(WeatherCondition.PARTLY_CLOUDY, 2.toWeatherCondition())
        assertEquals(WeatherCondition.CLOUDY, 3.toWeatherCondition())
        assertEquals(WeatherCondition.FOG, 45.toWeatherCondition())
        assertEquals(WeatherCondition.DRIZZLE, 51.toWeatherCondition())
        assertEquals(WeatherCondition.RAIN, 61.toWeatherCondition())
        assertEquals(WeatherCondition.SNOW, 71.toWeatherCondition())
        assertEquals(WeatherCondition.THUNDERSTORM, 95.toWeatherCondition())
        assertEquals(WeatherCondition.CLOUDY, 999.toWeatherCondition())
        assertEquals(WeatherCondition.CLOUDY, null.toWeatherCondition())
    }

    private fun readResource(path: String): String =
        checkNotNull(javaClass.getResource(path)) { "Missing test resource: $path" }
            .readText()
}
