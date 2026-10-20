package com.medioka.guideline.app.domain.error

/**
 * Failures the domain understands.
 *
 * Data operations and platform helpers return these through [Result.failure], so
 * the UI layer can decide which message or retry action to show without knowing
 * whether the problem came from Retrofit, the device location, or JSON parsing.
 */
sealed class WeatherException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {

    /** The device location could not be resolved, or permission was denied. */
    class LocationUnavailable(cause: Throwable? = null) : WeatherException(
        message = "Location is unavailable",
        cause = cause,
    )

    /** The weather service could not be reached. */
    class Network(cause: Throwable) : WeatherException(
        message = "The weather service could not be reached",
        cause = cause,
    )

    /** The weather service responded with a non-success HTTP status. */
    class Http(val statusCode: Int) : WeatherException(
        message = "The weather service returned HTTP $statusCode",
    )

    /** The response was valid HTTP but could not be mapped to domain models. */
    class Parsing(cause: Throwable) : WeatherException(
        message = "The weather response could not be read",
        cause = cause,
    )

    /** Coordinates outside the valid WGS84 ranges. */
    class InvalidLocation : WeatherException(
        message = "Latitude must be between -90 and 90 and longitude between -180 and 180",
    )

    /** Anything that does not fit the categories above. */
    class Unknown(cause: Throwable) : WeatherException(
        message = "An unexpected weather error occurred",
        cause = cause,
    )
}
