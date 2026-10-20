package com.medioka.guideline.app.presentation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.medioka.guideline.app.presentation.common.model.DailyForecast
import com.medioka.guideline.app.presentation.detail.DetailScreen
import com.medioka.guideline.app.presentation.detail.navigation.DailyForecastNavType
import com.medioka.guideline.app.presentation.detail.navigation.DetailRoute
import com.medioka.guideline.app.presentation.main.MainScreens
import com.medioka.guideline.app.presentation.main.navigation.MainScreensRoute
import kotlin.reflect.typeOf

/**
 * Root navigation host for the main screens and forecast details.
 */
@Composable
fun WeatherApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainScreensRoute,
        modifier = modifier.fillMaxSize(),
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(340),
                initialOffsetX = { fullWidth -> fullWidth },
            )
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(340),
                targetOffsetX = { fullWidth -> -fullWidth / 4 },
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                animationSpec = tween(340),
                initialOffsetX = { fullWidth -> -fullWidth / 4 },
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                animationSpec = tween(340),
                targetOffsetX = { fullWidth -> fullWidth },
            )
        },
    ) {
        composable<MainScreensRoute> {
            MainScreens(
                onNavigateToWeatherDay = { day, locationName ->
                    navController.navigate(DetailRoute(day, locationName))
                }
            )
        }

        composable<DetailRoute>(
            typeMap = mapOf(typeOf<DailyForecast>() to DailyForecastNavType()),
        ) { backStackEntry ->
            val destination = backStackEntry.toRoute<DetailRoute>()

            DetailScreen(
                day = destination.day,
                locationName = destination.locationName,
                onBack = navController::popBackStack
            )
        }
    }
}
