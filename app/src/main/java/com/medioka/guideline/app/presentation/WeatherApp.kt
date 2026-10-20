package com.medioka.guideline.app.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.medioka.guideline.app.presentation.model.DailyForecast
import com.medioka.guideline.app.presentation.model.WeatherUiState

/**
 * UI-only entry point. Navigation is kept local to this composable until a
 * navigation graph / ViewModel is introduced.
 */
@Composable
fun WeatherApp(
    state: WeatherUiState,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    onRetry: () -> Unit = {},
    onDaySelected: (DailyForecast) -> Unit = {},
) {
    var selectedDay by remember { mutableStateOf<DailyForecast?>(null) }

    LaunchedEffect(state) {
        if (state !is WeatherUiState.Success) {
            selectedDay = null
        }
    }

    BackHandler(enabled = selectedDay != null) {
        selectedDay = null
    }

    val locationName = (state as? WeatherUiState.Success)?.current?.locationName.orEmpty()

    AnimatedContent(
        targetState = selectedDay,
        modifier = modifier.fillMaxSize(),
        transitionSpec = {
            if (targetState != null) {
                (
                    slideInHorizontally(animationSpec = tween(340)) { fullWidth -> fullWidth } +
                        fadeIn(animationSpec = tween(240))
                    ) togetherWith (
                    slideOutHorizontally(animationSpec = tween(340)) { fullWidth -> -fullWidth / 4 } +
                        fadeOut(animationSpec = tween(200))
                    )
            } else {
                (
                    slideInHorizontally(animationSpec = tween(340)) { fullWidth -> -fullWidth / 4 } +
                        fadeIn(animationSpec = tween(240))
                    ) togetherWith (
                    slideOutHorizontally(animationSpec = tween(340)) { fullWidth -> fullWidth } +
                        fadeOut(animationSpec = tween(200))
                    )
            }
        },
        contentKey = { day -> day?.dateLabel ?: "home" },
        label = "weather-navigation",
    ) { day ->
        if (day == null) {
            HomeScreen(
                state = state,
                onRefresh = onRefresh,
                onRetry = onRetry,
                onDayClick = { forecast ->
                    selectedDay = forecast
                    onDaySelected(forecast)
                },
            )
        } else {
            DetailScreen(
                day = day,
                locationName = locationName,
                onBack = { selectedDay = null },
            )
        }
    }
}
