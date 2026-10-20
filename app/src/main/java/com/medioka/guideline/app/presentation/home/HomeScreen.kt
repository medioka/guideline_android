package com.medioka.guideline.app.presentation.home

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

/**
 * UI callbacks consumed by the Home screen.
 *
 * Android permission and location APIs stay outside this contract. The caller
 * translates their outcome into [LocationResult] and calls [onLocationResult].
 */
data class HomeCallbacks(
    val onScreenStarted: () -> Unit = {},
    val onGetLocation: () -> Unit = {},
    val onRefresh: () -> Unit = {},
    val onRetry: () -> Unit = {},
    val onLocationResult: (LocationResult) -> Unit = {},
    val onDismissError: () -> Unit = {},
    val onOpenSettings: () -> Unit = {},
    val isRefreshing: Boolean = false,
)

/**
 * Home destination.
 *
 * Owns the Home state and callbacks for now.
 *
 * TODO: replace the sample state and no-op callbacks with WeatherViewModel.
 */
@Composable
fun HomeScreen(
    onNavigateToWeatherDay: (DailyForecast, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: replace with the ViewModel StateFlow.
    val state: WeatherUiState = WeatherSampleData.success
    val callbacks = HomeCallbacks()

    LaunchedEffect(Unit) {
        // Only request initial data while loading, so returning from Detail
        // (state = Success) does not trigger another fetch.
        if (state is WeatherUiState.Loading) {
            callbacks.onScreenStarted()
        }
    }

    val locationName = (state as? WeatherUiState.Success)?.current?.locationName.orEmpty()

    HomeContent(
        state = state,
        callbacks = callbacks,
        onDayClick = { forecast -> onNavigateToWeatherDay(forecast, locationName) },
        modifier = modifier,
    )
}

/**
 * Stateless Home UI. Kept private so previews can render each state.
 */
@Composable
private fun HomeContent(
    state: WeatherUiState,
    callbacks: HomeCallbacks,
    onDayClick: (DailyForecast) -> Unit,
    modifier: Modifier = Modifier,
) {
    var lastSuccess by remember { mutableStateOf<WeatherUiState.Success?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state) {
        if (state is WeatherUiState.Success) lastSuccess = state
    }

    val refreshError = (state as? WeatherUiState.Error).takeIf { lastSuccess != null }
    val refreshErrorMessage = when (refreshError?.reason) {
        WeatherErrorReason.LOCATION -> stringResource(R.string.refresh_error_location)
        WeatherErrorReason.NETWORK -> stringResource(R.string.refresh_error_network)
        null -> null
    }
    LaunchedEffect(refreshErrorMessage) {
        refreshErrorMessage?.let { snackbarHostState.showSnackbar(it) }
    }

    val keepCachedContent = lastSuccess != null &&
        (refreshError != null || (state is WeatherUiState.Loading && callbacks.isRefreshing))
    val displayedState = if (keepCachedContent) lastSuccess!! else state

    Box(modifier = modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = displayedState,
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
                    onRetry = callbacks.onRetry,
                )

                is WeatherUiState.Success -> HomeSuccessContent(
                    current = uiState.current,
                    forecast = uiState.forecast,
                    isRefreshing = callbacks.isRefreshing && refreshError == null,
                    onRefresh = callbacks.onRefresh,
                    onDayClick = onDayClick,
                )
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
            callbacks = HomeCallbacks(),
            onDayClick = {},
        )
    }
}

@Preview(name = "Home - loading", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HomeLoadingPreview() {
    GuidelineAppTheme(darkTheme = true) {
        HomeContent(
            state = WeatherSampleData.loading,
            callbacks = HomeCallbacks(),
            onDayClick = {},
        )
    }
}

@Preview(name = "Home - location error", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HomeLocationErrorPreview() {
    GuidelineAppTheme(darkTheme = true) {
        HomeContent(
            state = WeatherSampleData.locationError,
            callbacks = HomeCallbacks(),
            onDayClick = {},
        )
    }
}

@Preview(name = "Home - network error", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HomeNetworkErrorPreview() {
    GuidelineAppTheme(darkTheme = true) {
        HomeContent(
            state = WeatherSampleData.networkError,
            callbacks = HomeCallbacks(),
            onDayClick = {},
        )
    }
}
