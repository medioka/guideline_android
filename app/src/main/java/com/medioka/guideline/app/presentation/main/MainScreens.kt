package com.medioka.guideline.app.presentation.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.medioka.guideline.app.presentation.common.model.DailyForecast
import com.medioka.guideline.app.presentation.home.HomeScreen
import com.medioka.guideline.app.presentation.home.navigation.HomeRoute
import com.medioka.guideline.app.presentation.main.navigation.RandomRoute
import com.medioka.guideline.app.presentation.main.navigation.SettingsRoute
import com.medioka.guideline.app.presentation.random.RandomScreen
import com.medioka.guideline.app.presentation.settings.SettingsScreen

private enum class MainTab(
    val label: String,
    val icon: ImageVector,
) {
    Home("Home", Icons.Filled.Home),
    Random("Random", Icons.Filled.Refresh),
    Settings("Settings", Icons.Filled.Settings),
}

/** Bottom-navigation host for the app's primary destinations. */
@Composable
fun MainScreens(
    onNavigateToWeatherDay: (DailyForecast, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar {
                MainTab.entries.forEach { tab ->
                    val selected = when (tab) {
                        MainTab.Home -> currentDestination?.hasRoute<HomeRoute>() == true
                        MainTab.Random -> currentDestination?.hasRoute<RandomRoute>() == true
                        MainTab.Settings -> currentDestination?.hasRoute<SettingsRoute>() == true
                    }
                    NavigationBarItem(
                        selected = selected,
                        onClick = { navController.navigateToTab(tab) },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                    )
                }
            }
        },
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(contentPadding),
        ) {
            composable<HomeRoute> {
                HomeScreen(onNavigateToWeatherDay = onNavigateToWeatherDay)
            }
            composable<RandomRoute> {
                RandomScreen()
            }
            composable<SettingsRoute> {
                SettingsScreen()
            }
        }
    }
}

private fun NavHostController.navigateToTab(tab: MainTab) {
    val startDestinationId = graph.findStartDestination().id
    val options: NavOptionsBuilder.() -> Unit = {
        popUpTo(startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
    when (tab) {
        MainTab.Home -> navigate(HomeRoute, options)
        MainTab.Random -> navigate(RandomRoute, options)
        MainTab.Settings -> navigate(SettingsRoute, options)
    }
}
