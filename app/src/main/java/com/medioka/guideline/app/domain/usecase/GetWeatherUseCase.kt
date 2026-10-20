package com.medioka.guideline.app.domain.usecase

import com.medioka.guideline.app.domain.error.WeatherException
import com.medioka.guideline.app.domain.model.Weather
import com.medioka.guideline.app.domain.payload.WeatherPayload
import com.medioka.guideline.app.domain.repository.WeatherRepository

/**
 * Fetches a forecast for the coordinates in [WeatherPayload].
 *
 * Presentation resolves the device location and builds the payload. This use
 * case validates it and hands it to the repository. The data layer then
 * converts the payload into its own request model.
 */
class GetWeatherUseCase(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(payload: WeatherPayload): Weather {
        if (payload.latitude !in -90.0..90.0 || payload.longitude !in -180.0..180.0) {
            throw WeatherException.InvalidLocation()
        }
        return repository.getWeather(payload)
    }
}
