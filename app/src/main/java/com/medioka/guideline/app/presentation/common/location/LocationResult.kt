package com.medioka.guideline.app.presentation.common.location

import com.medioka.guideline.app.domain.payload.WeatherPayload

/**
 * Result of the Android permission + location flow.
 *
 * The screen translates platform permission and location outcomes into this
 * type, keeping Android APIs and [Result] out of the ViewModel.
 */
sealed interface LocationResult {
    data class Available(val location: WeatherPayload) : LocationResult

    data object PermissionDenied : LocationResult

    data object Unavailable : LocationResult
}
