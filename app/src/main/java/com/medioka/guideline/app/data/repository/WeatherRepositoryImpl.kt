package com.medioka.guideline.app.data.repository

import com.medioka.guideline.app.data.mapper.toDomain
import com.medioka.guideline.app.data.mapper.toWeatherException
import com.medioka.guideline.app.data.remote.WeatherApi
import com.medioka.guideline.app.data.remote.request.ForecastRequest
import com.medioka.guideline.app.domain.model.Weather
import com.medioka.guideline.app.domain.model.WeatherLocation
import com.medioka.guideline.app.domain.payload.WeatherPayload
import com.medioka.guideline.app.domain.repository.WeatherRepository

/**
 * Data-layer implementation of the domain port.
 *
 * Responsibilities:
 * 1. call the remote API,
 * 2. map API responses into domain models,
 * 3. translate infrastructure exceptions into [com.medioka.guideline.app.domain.error.WeatherException].
 *
 * The ViewModel/use case never sees Retrofit or serialization types.
 */
class WeatherRepositoryImpl(
    private val api: WeatherApi,
) : WeatherRepository {

    override suspend fun getWeather(payload: WeatherPayload): Weather {
        val location = WeatherLocation(
            latitude = payload.latitude,
            longitude = payload.longitude,
        )

        return try {
            api.getForecast(
                ForecastRequest(
                    latitude = payload.latitude,
                    longitude = payload.longitude,
                ).toQueryMap(),
            ).toDomain(location)
        } catch (e: Exception) {
            throw e.toWeatherException()
        }
    }
}
