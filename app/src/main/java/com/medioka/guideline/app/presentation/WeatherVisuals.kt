package com.medioka.guideline.app.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.medioka.guideline.app.R
import com.medioka.guideline.app.presentation.model.WeatherCondition

/**
 * Colors used for the full-screen weather backdrop. The actual content always
 * sits on top of a dark enough gradient so white foreground tokens stay legible.
 */
data class WeatherBackdrop(
    val top: Color,
    val bottom: Color,
    val glow: Color,
)

fun weatherBackdrop(condition: WeatherCondition, isDay: Boolean): WeatherBackdrop =
    if (isDay) {
        dayBackdrop(condition)
    } else {
        nightBackdrop(condition)
    }

private fun dayBackdrop(condition: WeatherCondition): WeatherBackdrop = when (condition) {
    WeatherCondition.CLEAR -> WeatherBackdrop(
        top = Color(0xFF2C7BE5),
        bottom = Color(0xFF6EC4F7),
        glow = Color(0xFFFFE08A),
    )

    WeatherCondition.PARTLY_CLOUDY -> WeatherBackdrop(
        top = Color(0xFF2F6DBE),
        bottom = Color(0xFF78A9DC),
        glow = Color(0xFFFFD98A),
    )

    WeatherCondition.CLOUDY -> WeatherBackdrop(
        top = Color(0xFF485A73),
        bottom = Color(0xFF7D90A9),
        glow = Color(0xFFD9E6FF),
    )

    WeatherCondition.FOG -> WeatherBackdrop(
        top = Color(0xFF4E5E73),
        bottom = Color(0xFF8494A7),
        glow = Color(0xFFD9E2EC),
    )

    WeatherCondition.DRIZZLE -> WeatherBackdrop(
        top = Color(0xFF2E4A66),
        bottom = Color(0xFF5D7E9C),
        glow = Color(0xFF8FD3FF),
    )

    WeatherCondition.RAIN -> WeatherBackdrop(
        top = Color(0xFF203A55),
        bottom = Color(0xFF4B6B8C),
        glow = Color(0xFF8FD3FF),
    )

    WeatherCondition.SNOW -> WeatherBackdrop(
        top = Color(0xFF3F5F80),
        bottom = Color(0xFF86A9C6),
        glow = Color(0xFFE6F4FF),
    )

    WeatherCondition.THUNDERSTORM -> WeatherBackdrop(
        top = Color(0xFF171C2C),
        bottom = Color(0xFF3A4260),
        glow = Color(0xFFC6B7FF),
    )
}

private fun nightBackdrop(condition: WeatherCondition): WeatherBackdrop = when (condition) {
    WeatherCondition.RAIN,
    WeatherCondition.DRIZZLE,
    -> WeatherBackdrop(
        top = Color(0xFF0B1A2B),
        bottom = Color(0xFF25384F),
        glow = Color(0xFF7BC4FF),
    )

    WeatherCondition.THUNDERSTORM -> WeatherBackdrop(
        top = Color(0xFF100E1E),
        bottom = Color(0xFF2C2747),
        glow = Color(0xFFC6B7FF),
    )

    WeatherCondition.SNOW -> WeatherBackdrop(
        top = Color(0xFF0E1B2B),
        bottom = Color(0xFF2E4861),
        glow = Color(0xFFDCEBFF),
    )

    else -> WeatherBackdrop(
        top = Color(0xFF060D1C),
        bottom = Color(0xFF1A2C4D),
        glow = Color(0xFF9FB8FF),
    )
}

/** Neutral backdrop used while data is still being resolved. */
val LoadingBackdrop = WeatherBackdrop(
    top = Color(0xFF16233A),
    bottom = Color(0xFF2B4060),
    glow = Color(0xFF6FA8FF),
)

/** Neutral backdrop used for both location and network failures. */
val ErrorBackdrop = WeatherBackdrop(
    top = Color(0xFF151F32),
    bottom = Color(0xFF2D3E58),
    glow = Color(0xFFFFB199),
)

@Composable
fun weatherConditionLabel(condition: WeatherCondition, isDay: Boolean): String = when (condition) {
    WeatherCondition.CLEAR ->
        stringResource(if (isDay) R.string.condition_sunny else R.string.condition_clear)

    WeatherCondition.PARTLY_CLOUDY -> stringResource(R.string.condition_partly_cloudy)
    WeatherCondition.CLOUDY -> stringResource(R.string.condition_cloudy)
    WeatherCondition.FOG -> stringResource(R.string.condition_foggy)
    WeatherCondition.DRIZZLE -> stringResource(R.string.condition_drizzle)
    WeatherCondition.RAIN -> stringResource(R.string.condition_rain)
    WeatherCondition.SNOW -> stringResource(R.string.condition_snow)
    WeatherCondition.THUNDERSTORM -> stringResource(R.string.condition_thunderstorm)
}
