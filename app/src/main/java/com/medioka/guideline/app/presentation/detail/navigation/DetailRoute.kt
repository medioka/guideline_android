package com.medioka.guideline.app.presentation.detail.navigation

import com.medioka.guideline.app.presentation.common.model.DailyForecast
import kotlinx.serialization.Serializable

@Serializable
data class DetailRoute(
    val day: DailyForecast,
    val locationName: String,
)
