package com.medioka.guideline.app.data.repository

import com.medioka.guideline.app.data.remote.WeatherApi
import com.medioka.guideline.app.data.remote.response.ForecastResponse
import com.medioka.guideline.app.domain.error.WeatherException
import com.medioka.guideline.app.domain.payload.WeatherPayload
import com.medioka.guideline.app.testJson
import java.io.IOException
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class WeatherRepositoryImplTest {

    private val response: ForecastResponse = testJson
        .decodeFromString(readResource("/open_meteo_response.json"))

    @Test
    fun `returns a mapped domain model`() = runTest {
        val api = FakeWeatherApi(response = response)
        val repository = WeatherRepositoryImpl(api)
        val payload = WeatherPayload(latitude = 37.7749, longitude = -122.4194)

        val weather = repository.getWeather(payload)

        assertEquals(payload.latitude, weather.location.latitude, 0.001)
        assertEquals(payload.longitude, weather.location.longitude, 0.001)
        assertEquals(2, weather.daily.size)
        assertEquals(payload.latitude, api.requestedLatitude ?: 0.0, 0.001)
        assertEquals(payload.longitude, api.requestedLongitude ?: 0.0, 0.001)
    }

    @Test
    fun `maps IOException to a network failure`() = runTest {
        val repository = WeatherRepositoryImpl(
            FakeWeatherApi(error = IOException("offline")),
        )

        val result = runCatching { repository.getWeather(WeatherPayload()) }

        assertTrue(result.exceptionOrNull() is WeatherException.Network)
    }

    @Test
    fun `maps HTTP errors to a typed failure`() = runTest {
        val httpError = HttpException(
            Response.error<ForecastResponse>(
                503,
                """{"error":true}""".toResponseBody("application/json".toMediaType()),
            ),
        )
        val repository = WeatherRepositoryImpl(FakeWeatherApi(error = httpError))

        val result = runCatching { repository.getWeather(WeatherPayload()) }

        val error = result.exceptionOrNull()
        assertTrue(error is WeatherException.Http)
        assertEquals(503, (error as WeatherException.Http).statusCode)
    }

    private class FakeWeatherApi(
        private val response: ForecastResponse? = null,
        private val error: Throwable? = null,
    ) : WeatherApi {
        var requestedLatitude: Double? = null
            private set
        var requestedLongitude: Double? = null
            private set

        override suspend fun getForecast(query: Map<String, String>): ForecastResponse {
            requestedLatitude = query["latitude"]?.toDoubleOrNull()
            requestedLongitude = query["longitude"]?.toDoubleOrNull()
            error?.let { throw it }
            return checkNotNull(response)
        }
    }

    private fun readResource(path: String): String =
        checkNotNull(javaClass.getResource(path)) { "Missing test resource: $path" }
            .readText()
}
