package com.medioka.guideline.app.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medioka.guideline.app.R
import com.medioka.guideline.app.presentation.common.components.ScreenFooter
import com.medioka.guideline.app.presentation.common.components.SectionHeader
import com.medioka.guideline.app.presentation.common.components.WeatherBackground
import com.medioka.guideline.app.presentation.common.components.WeatherDetail
import com.medioka.guideline.app.presentation.common.components.WeatherDetailsGrid
import com.medioka.guideline.app.presentation.common.components.WeatherGlyph
import com.medioka.guideline.app.presentation.common.extension.formatDecimal
import com.medioka.guideline.app.presentation.common.model.DailyForecast
import com.medioka.guideline.app.presentation.common.previews.WeatherSampleData
import com.medioka.guideline.app.presentation.detail.components.DetailTopBar
import com.medioka.guideline.app.presentation.detail.components.HourlyCard
import com.medioka.guideline.app.presentation.detail.components.SunTimesCard
import com.medioka.guideline.app.presentation.detail.components.TemperatureRangeBar
import com.medioka.guideline.app.presentation.weatherBackdrop
import com.medioka.guideline.app.presentation.weatherConditionLabel
import com.medioka.guideline.app.theme.GuidelineAppTheme

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    day: DailyForecast,
    locationName: String,
    onBack: () -> Unit
) {
    val backdrop = weatherBackdrop(condition = day.condition, isDay = true)
    val details = listOf(
        WeatherDetail(
            label = stringResource(R.string.humidity),
            value = stringResource(R.string.humidity_format, day.humidityPercent),
        ),
        WeatherDetail(
            label = stringResource(R.string.wind),
            value = stringResource(R.string.wind_format, day.windSpeedKmh, day.windDirection),
        ),
        WeatherDetail(
            label = stringResource(R.string.rain_chance),
            value = stringResource(
                R.string.percent_format,
                day.precipitationProbabilityPercent,
            ),
        ),
        WeatherDetail(
            label = stringResource(R.string.precipitation),
            value = stringResource(
                R.string.precipitation_format,
                day.precipitationMm.formatDecimal()
            ),
        ),
        WeatherDetail(
            label = stringResource(R.string.uv_index),
            value = day.uvIndex.toString(),
        ),
        WeatherDetail(
            label = stringResource(R.string.pressure),
            value = stringResource(R.string.pressure_format, day.pressureHpa),
        ),
    )

    WeatherBackground(backdrop = backdrop, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            DetailTopBar(
                day = day,
                locationName = locationName,
                onBack = onBack,
            )

            Spacer(modifier = Modifier.height(22.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                WeatherGlyph(
                    condition = day.condition,
                    isDay = true,
                    modifier = Modifier.size(144.dp),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = weatherConditionLabel(condition = day.condition, isDay = true),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White.copy(alpha = 0.94f),
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = stringResource(R.string.temperature_format, day.highCelsius),
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.temperature_format, day.lowCelsius),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White.copy(alpha = 0.48f),
                        modifier = Modifier.padding(bottom = 18.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            TemperatureRangeBar(lowCelsius = day.lowCelsius, highCelsius = day.highCelsius)

            if (day.hourly.isNotEmpty()) {
                Spacer(modifier = Modifier.height(30.dp))
                SectionHeader(title = stringResource(R.string.hourly_forecast))
                Spacer(modifier = Modifier.height(14.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(items = day.hourly, key = { it.timeLabel }) { hour ->
                        HourlyCard(hour = hour)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            SectionHeader(title = stringResource(R.string.day_details))
            Spacer(modifier = Modifier.height(12.dp))
            WeatherDetailsGrid(details = details)

            Spacer(modifier = Modifier.height(10.dp))
            SunTimesCard(
                sunriseLabel = day.sunriseLabel,
                sunsetLabel = day.sunsetLabel,
            )

            ScreenFooter(text = stringResource(R.string.powered_by))
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(name = "Detail - rainy day", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun DetailRainyPreview() {
    GuidelineAppTheme(darkTheme = true) {
        DetailScreen(
            day = WeatherSampleData.forecast[2],
            locationName = WeatherSampleData.current.locationName,
            onBack = {},
        )
    }
}

@Preview(name = "Detail - sunny day", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun DetailSunnyPreview() {
    GuidelineAppTheme(darkTheme = true) {
        DetailScreen(
            day = WeatherSampleData.forecast[1],
            locationName = WeatherSampleData.current.locationName,
            onBack = {},
        )
    }
}
