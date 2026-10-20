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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.medioka.guideline.app.presentation.common.model.DailyForecast
import com.medioka.guideline.app.presentation.detail.DetailScreen
import com.medioka.guideline.app.presentation.home.HomeScreen

/**
 * Navigation host for Home and Detail.
 *
 * HomeScreen owns its state and callbacks. This composable only keeps the local
 * navigation state for now.
 */
@Composable
fun WeatherApp(modifier: Modifier = Modifier) {
    var selectedDay by remember { mutableStateOf<DailyForecast?>(null) }
    var locationName by remember { mutableStateOf("") }

    BackHandler(enabled = selectedDay != null) {
        selectedDay = null
    }

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
                onDaySelected = { forecast, location ->
                    selectedDay = forecast
                    locationName = location
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
