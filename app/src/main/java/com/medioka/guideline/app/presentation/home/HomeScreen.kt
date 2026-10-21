package com.medioka.guideline.app.presentation.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medioka.guideline.app.R
import com.medioka.guideline.app.presentation.common.components.HomeErrorContent
import com.medioka.guideline.app.presentation.common.components.HomeLoadingContent
import com.medioka.guideline.app.presentation.common.components.ScreenFooter
import com.medioka.guideline.app.presentation.common.components.SectionHeader
import com.medioka.guideline.app.presentation.common.components.WeatherBackground
import com.medioka.guideline.app.presentation.common.components.WeatherDetail
import com.medioka.guideline.app.presentation.common.components.WeatherDetailsGrid
import com.medioka.guideline.app.presentation.common.extension.formatDecimal
import com.medioka.guideline.app.presentation.common.location.LocationResult
import com.medioka.guideline.app.presentation.common.location.hasLocationPermission
import com.medioka.guideline.app.presentation.common.model.CurrentWeather
import com.medioka.guideline.app.presentation.common.model.DailyForecast
import com.medioka.guideline.app.presentation.common.model.WeatherErrorReason
import com.medioka.guideline.app.presentation.common.model.WeatherUiState
import com.medioka.guideline.app.presentation.common.previews.WeatherSampleData
import com.medioka.guideline.app.presentation.home.components.CurrentWeatherHero
import com.medioka.guideline.app.presentation.home.components.ForecastCard
import com.medioka.guideline.app.presentation.home.components.LocationBar
import com.medioka.guideline.app.presentation.weatherBackdrop
import com.medioka.guideline.app.theme.GuidelineAppTheme
import org.koin.compose.viewmodel.koinViewModel

/**
 * Home destination.
 *
 * The screen owns the Android permission/location flow and reports its outcome
 * to [HomeViewModel]. All weather state and use case calls live in the
 * ViewModel.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToWeatherDay: (DailyForecast, String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = LocalActivity.current ?: throw IllegalStateException("LocalActivity is required")
    val snackbarHostState = remember { SnackbarHostState() }
    val locationRefreshError = stringResource(R.string.refresh_error_location)
    val networkRefreshError = stringResource(R.string.refresh_error_network)

    val permissionLauncher: ActivityResultLauncher<String> = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted || activity.hasLocationPermission()) {
            viewModel.getCurrentLocation()
        } else {
            viewModel.handleLocationResult(LocationResult.PermissionDenied)
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.locationRequestEvent.collect {
            viewModel.askPermissionAndRequestLocation(
                activity = activity,
                onRequestPermission = {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            )
        }
    }

    LaunchedEffect(viewModel, locationRefreshError, networkRefreshError) {
        viewModel.refreshErrorEvents.collect { reason ->
            val message = when (reason) {
                WeatherErrorReason.LOCATION -> locationRefreshError
                WeatherErrorReason.NETWORK -> networkRefreshError
            }
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    HomeContent(
        modifier = modifier,
        state = state,
        isRefreshing = isRefreshing,
        onRetry = viewModel::onRetry,
        onRefresh = viewModel::onRefresh,
        onOpenSettings = { context.openAppSettings() },
        onDayClick = onNavigateToWeatherDay,
        snackbarHostState = snackbarHostState,
    )
}

/**
 * Stateless Home UI. Kept private so previews can render each state.
 */
@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    state: WeatherUiState,
    isRefreshing: Boolean,
    onRetry: () -> Unit,
    onOpenSettings: () -> Unit,
    onRefresh: () -> Unit,
    onDayClick: (DailyForecast, String) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Box(modifier = modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = state,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                fadeIn(animationSpec = tween(420)) togetherWith fadeOut(animationSpec = tween(200))
            },
            contentKey = { it::class },
            label = "weather-state",
        ) { uiState ->
            when (uiState) {
                WeatherUiState.Loading -> HomeLoadingContent()
                is WeatherUiState.Error -> HomeErrorContent(
                    error = uiState,
                    onRetry = onRetry,
                    onOpenSettings = onOpenSettings,
                )

                is WeatherUiState.Success -> {
                    val locationName = uiState.current.locationName
                    HomeSuccessContent(
                        current = uiState.current,
                        forecast = uiState.forecast,
                        isRefreshing = isRefreshing,
                        onRefresh = onRefresh,
                        onDayClick = { forecast ->
                            onDayClick(forecast, locationName)
                        }
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeSuccessContent(
    current: CurrentWeather,
    forecast: List<DailyForecast>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onDayClick: (DailyForecast) -> Unit,
) {
    val backdrop = weatherBackdrop(condition = current.condition, isDay = current.isDay)
    val details = listOf(
        WeatherDetail(
            label = stringResource(R.string.feels_like),
            value = stringResource(R.string.temperature_format, current.feelsLikeCelsius),
        ),
        WeatherDetail(
            label = stringResource(R.string.humidity),
            value = stringResource(R.string.humidity_format, current.humidityPercent),
        ),
        WeatherDetail(
            label = stringResource(R.string.wind),
            value = stringResource(
                R.string.wind_format,
                current.windSpeedKmh,
                current.windDirection,
            ),
        ),
        WeatherDetail(
            label = stringResource(R.string.uv_index),
            value = current.uvIndex.toString(),
        ),
        WeatherDetail(
            label = stringResource(R.string.pressure),
            value = stringResource(R.string.pressure_format, current.pressureHpa),
        ),
        WeatherDetail(
            label = stringResource(R.string.visibility),
            value = stringResource(
                R.string.visibility_format,
                current.visibilityKm.formatDecimal()
            ),
        ),
    )

    WeatherBackground(backdrop = backdrop) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .statusBarsPadding()
                    .navigationBarsPadding(),
            ) {
                LocationBar(current = current)

                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Spacer(modifier = Modifier.height(20.dp))
                    CurrentWeatherHero(current = current)
                    Spacer(modifier = Modifier.height(30.dp))
                    SectionHeader(title = stringResource(R.string.todays_details))
                    Spacer(modifier = Modifier.height(12.dp))
                    WeatherDetailsGrid(details = details)
                    Spacer(modifier = Modifier.height(30.dp))
                    SectionHeader(
                        title = stringResource(R.string.seven_day_forecast),
                        supportingText = stringResource(R.string.forecast_hint),
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    itemsIndexed(
                        items = forecast,
                        key = { _, day -> "${day.dateLabel}-${day.dayLabel}" },
                    ) { index, day ->
                        ForecastCard(
                            day = day,
                            highlighted = index == 0,
                            onClick = { onDayClick(day) },
                        )
                    }
                }

                ScreenFooter(text = stringResource(R.string.powered_by))
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(name = "Home - success", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HomeSuccessPreview() {
    GuidelineAppTheme(darkTheme = true) {
        HomeContent(
                state = WeatherSampleData.success,
                isRefreshing = false,
                onRefresh = {},
                onRetry = {},
                onDayClick = { _, _ -> },
                onOpenSettings = {},
                snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

@Preview(name = "Home - loading", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HomeLoadingPreview() {
    GuidelineAppTheme(darkTheme = true) {
        HomeContent(
            state = WeatherSampleData.loading,
            isRefreshing = false,
            onOpenSettings = {},
                onRetry = {},
                onRefresh = {},
                onDayClick = { _, _ -> },
                snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

@Preview(name = "Home - location error", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HomeLocationErrorPreview() {
    GuidelineAppTheme(darkTheme = true) {
        HomeContent(
            state = WeatherSampleData.locationError,
            isRefreshing = false,
            onRefresh = {},
                onDayClick = { _, _ -> },
                onRetry = {},
                onOpenSettings = {},
                snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

@Preview(name = "Home - network error", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HomeNetworkErrorPreview() {
    GuidelineAppTheme(darkTheme = true) {
        HomeContent(
            state = WeatherSampleData.networkError,
            isRefreshing = false,
            onRetry = {},
                onOpenSettings = {},
                onRefresh = {},
                onDayClick = { _, _ -> },
                snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

private fun Context.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null),
    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
