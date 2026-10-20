package com.medioka.guideline.app.domain.usecase

import com.medioka.guideline.app.domain.error.WeatherException
import com.medioka.guideline.app.domain.model.Weather
import com.medioka.guideline.app.domain.payload.WeatherPayload
import com.medioka.guideline.app.domain.repository.WeatherRepository
import com.medioka.guideline.app.domain.testWeather
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetWeatherUseCaseTest {

    @Test
    fun `returns the repository result for valid coordinates`() = runTest {
        val expected = testWeather()
        val repository = FakeWeatherRepository(Result.success(expected))
        val useCase = GetWeatherUseCase(repository)
        val payload = WeatherPayload(latitude = 37.7749, longitude = -122.4194)

        val result = useCase(payload)

        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
        assertEquals(payload, repository.requestedPayload)
    }

    @Test
    fun `rejects a latitude outside the valid range`() = runTest {
        val repository = FakeWeatherRepository(Result.success(testWeather()))
        val useCase = GetWeatherUseCase(repository)

        val result = useCase(WeatherPayload(latitude = 91.0, longitude = -122.4194))

        assertTrue(result.exceptionOrNull() is WeatherException.InvalidLocation)
        assertEquals(null, repository.requestedPayload)
    }

    @Test
    fun `rejects a longitude outside the valid range`() = runTest {
        val repository = FakeWeatherRepository(Result.success(testWeather()))
        val useCase = GetWeatherUseCase(repository)

        val result = useCase(WeatherPayload(latitude = 37.7749, longitude = -181.0))

        assertTrue(result.exceptionOrNull() is WeatherException.InvalidLocation)
        assertEquals(null, repository.requestedPayload)
    }

    private class FakeWeatherRepository(
        private val result: Result<Weather>,
    ) : WeatherRepository {
        var requestedPayload: WeatherPayload? = null
            private set

        override suspend fun getWeather(payload: WeatherPayload): Result<Weather> {
            requestedPayload = payload
            return result
        }
    }
}
