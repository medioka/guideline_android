package com.medioka.guideline.app.presentation.common.model
enum class WeatherErrorReason {
    /** The device location could not be resolved. */
    LOCATION,

    /** The forecast request failed (network, server, parsing, ...). */
    NETWORK,
}
