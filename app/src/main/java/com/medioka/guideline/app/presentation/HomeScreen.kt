package com.medioka.guideline.app.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medioka.guideline.app.R
import com.medioka.guideline.app.presentation.components.ScreenFooter
import com.medioka.guideline.app.presentation.components.SectionHeader
import com.medioka.guideline.app.presentation.components.WeatherBackground
import com.medioka.guideline.app.presentation.components.WeatherDetail
import com.medioka.guideline.app.presentation.components.WeatherDetailsGrid
import com.medioka.guideline.app.presentation.components.WeatherErrorContent
import com.medioka.guideline.app.presentation.components.WeatherLoadingContent
import com.medioka.guideline.app.presentation.home.components.CurrentWeatherHero
import com.medioka.guideline.app.presentation.home.components.ForecastCard
import com.medioka.guideline.app.presentation.home.components.LocationBar
import com.medioka.guideline.app.presentation.model.CurrentWeather
import com.medioka.guideline.app.presentation.model.DailyForecast
import com.medioka.guideline.app.presentation.model.WeatherUiState
import com.medioka.guideline.app.theme.GuidelineAppTheme

@Composable
fun HomeScreen(
    state: WeatherUiState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onDayClick: (DailyForecast) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = state,
        modifier = modifier.fillMaxSize(),
        transitionSpec = {
            fadeIn(animationSpec = tween(420)) togetherWith fadeOut(animationSpec = tween(200))
        },
        contentKey = { it::class },
        label = "weather-state",
    ) { uiState ->
        when (uiState) {
            WeatherUiState.Loading -> WeatherLoadingContent()
            is WeatherUiState.Error -> WeatherErrorContent(
                error = uiState,
                onRetry = onRetry,
            )

            is WeatherUiState.Success -> WeatherHomeContent(
                current = uiState.current,
                forecast = uiState.forecast,
                onRefresh = onRefresh,
                onDayClick = onDayClick,
            )
        }
    }
}

@Composable
private fun WeatherHomeContent(
    current: CurrentWeather,
    forecast: List<DailyForecast>,
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
                formatDecimal(current.visibilityKm),
            ),
        ),
    )

    WeatherBackground(backdrop = backdrop) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding(),
        ) {
            LocationBar(current = current, onRefresh = onRefresh)

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

@Preview(name = "Home - success", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HomeSuccessPreview() {
    GuidelineAppTheme(darkTheme = true) {
        HomeScreen(
            state = WeatherSampleData.success,
            onRefresh = {},
            onRetry = {},
            onDayClick = {},
        )
    }
}

@Preview(name = "Home - loading", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HomeLoadingPreview() {
    GuidelineAppTheme(darkTheme = true) {
        HomeScreen(
            state = WeatherSampleData.loading,
            onRefresh = {},
            onRetry = {},
            onDayClick = {},
        )
    }
}

@Preview(name = "Home - location error", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HomeLocationErrorPreview() {
    GuidelineAppTheme(darkTheme = true) {
        HomeScreen(
            state = WeatherSampleData.locationError,
            onRefresh = {},
            onRetry = {},
            onDayClick = {},
        )
    }
}

@Preview(name = "Home - network error", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HomeNetworkErrorPreview() {
    GuidelineAppTheme(darkTheme = true) {
        HomeScreen(
            state = WeatherSampleData.networkError,
            onRefresh = {},
            onRetry = {},
            onDayClick = {},
        )
    }
}
