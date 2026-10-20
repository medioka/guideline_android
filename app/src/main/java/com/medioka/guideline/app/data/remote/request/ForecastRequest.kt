package com.medioka.guideline.app.data.remote.request

/**
 * Query parameters for an Open-Meteo forecast request.
 *
 * This is a data-layer model: it knows the provider's query names and default
 * field lists. The domain only knows [com.medioka.guideline.app.domain.model.WeatherLocation].
 */
internal data class ForecastRequest(
    val latitude: Double,
    val longitude: Double,
    val current: String = CURRENT_FIELDS,
    val hourly: String = HOURLY_FIELDS,
    val daily: String = DAILY_FIELDS,
    val timezone: String = "auto",
    val forecastDays: Int = 7,
) {
    /**
     * Retrofit's [retrofit2.http.QueryMap] accepts a map, so the request object
     * is converted once here instead of leaking its fields into the API method.
     */
    fun toQueryMap(): Map<String, String> = mapOf(
        "latitude" to latitude.toString(),
        "longitude" to longitude.toString(),
        "current" to current,
        "hourly" to hourly,
        "daily" to daily,
        "timezone" to timezone,
        "forecast_days" to forecastDays.toString(),
    )
}

private const val CURRENT_FIELDS =
    "temperature_2m,relative_humidity_2m,apparent_temperature,is_day," +
        "precipitation,weather_code,pressure_msl,wind_speed_10m," +
        "wind_direction_10m,visibility"

private const val HOURLY_FIELDS =
    "temperature_2m,relative_humidity_2m,pressure_msl," +
        "precipitation_probability,weather_code,is_day"

private const val DAILY_FIELDS =
    "weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset," +
        "uv_index_max,precipitation_sum,precipitation_probability_max," +
        "wind_speed_10m_max,wind_direction_10m_dominant"
