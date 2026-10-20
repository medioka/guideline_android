package com.medioka.guideline.app.presentation.detail.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.medioka.guideline.app.presentation.common.model.DailyForecast
import kotlinx.serialization.json.Json

/**
 * Type-safe navigation cannot infer a NavType for a complex class like
 * [DailyForecast]. Store it as JSON and register this type on the destination.
 */
class DailyForecastNavType : NavType<DailyForecast>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): DailyForecast? =
        bundle.getString(key)?.let(::decode)

    override fun parseValue(value: String): DailyForecast =
        decode(Uri.decode(value))

    override fun put(bundle: Bundle, key: String, value: DailyForecast) {
        bundle.putString(key, encode(value))
    }

    override fun serializeAsValue(value: DailyForecast): String =
        Uri.encode(encode(value))

    private fun encode(value: DailyForecast): String =
        json.encodeToString(DailyForecast.serializer(), value)

    private fun decode(value: String): DailyForecast =
        json.decodeFromString(DailyForecast.serializer(), value)

    private companion object {
        val json = Json { ignoreUnknownKeys = true }
    }
}
