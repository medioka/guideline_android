package com.medioka.guideline.app.data.mapper

import com.medioka.guideline.app.domain.error.WeatherException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

/**
 * Converts infrastructure failures into domain failures once, at the data
 * boundary. Everything above this point can switch on [WeatherException].
 */
internal fun Throwable.toWeatherException(): WeatherException = when (this) {
    is WeatherException -> this
    is HttpException -> WeatherException.Http(code())
    is SerializationException -> WeatherException.Parsing(this)
    is IOException -> WeatherException.Network(this)
    is CancellationException -> throw this
    else -> WeatherException.Unknown(this)
}
