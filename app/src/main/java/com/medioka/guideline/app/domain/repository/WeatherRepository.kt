package com.medioka.guideline.app.domain.repository

import com.medioka.guideline.app.domain.model.Weather
import com.medioka.guideline.app.domain.payload.WeatherPayload

/**
 * Port implemented by the data layer.
 *
 * This interface is the dependency inversion point: domain and presentation
 * depend on it, while Retrofit and response models stay inside the data layer.
 */
interface WeatherRepository {
    suspend fun getWeather(payload: WeatherPayload): Weather
}
