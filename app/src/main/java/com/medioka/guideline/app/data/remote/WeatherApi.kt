package com.medioka.guideline.app.data.remote

import com.medioka.guideline.app.data.remote.response.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Retrofit description of the Open-Meteo forecast endpoint.
 *
 * Query values are grouped in ForecastRequest, so this contract stays small.
 * The repository maps [ForecastResponse] into domain models before returning it.
 */
interface WeatherApi {

    @GET("v1/forecast")
    suspend fun getForecast(@QueryMap query: Map<String, String>): ForecastResponse

    companion object {
        const val BASE_URL = "https://api.open-meteo.com/"
    }
}
