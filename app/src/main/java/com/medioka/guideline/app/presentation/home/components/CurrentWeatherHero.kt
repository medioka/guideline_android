package com.medioka.guideline.app.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.medioka.guideline.app.presentation.WeatherSampleData
import com.medioka.guideline.app.presentation.components.WeatherGlyph
import com.medioka.guideline.app.presentation.model.CurrentWeather
import com.medioka.guideline.app.presentation.weatherConditionLabel
import com.medioka.guideline.app.theme.GuidelineAppTheme

@Composable
fun CurrentWeatherHero(
    current: CurrentWeather,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(
                    R.string.temperature_format,
                    current.temperatureCelsius,
                ),
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
                maxLines = 1,
            )
            Text(
                text = weatherConditionLabel(
                    condition = current.condition,
                    isDay = current.isDay,
                ),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White.copy(alpha = 0.92f),
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(
                    R.string.high_low_format,
                    current.highCelsius,
                    current.lowCelsius,
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.72f),
            )
        }
        WeatherGlyph(
            condition = current.condition,
            isDay = current.isDay,
            modifier = Modifier.size(152.dp),
        )
    }
}

@Preview(name = "Current weather hero", showBackground = true, widthDp = 412, heightDp = 360)
@Composable
private fun CurrentWeatherHeroPreview() {
    GuidelineAppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2F6DBE))
                .padding(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            CurrentWeatherHero(current = WeatherSampleData.current)
        }
    }
}
