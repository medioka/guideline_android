package com.medioka.guideline.app.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medioka.guideline.app.R
import com.medioka.guideline.app.presentation.WeatherSampleData
import com.medioka.guideline.app.presentation.components.RainDrop
import com.medioka.guideline.app.presentation.components.WeatherGlyph
import com.medioka.guideline.app.presentation.model.HourlyForecast
import com.medioka.guideline.app.theme.GuidelineAppTheme

@Composable
fun HourlyCard(
    hour: HourlyForecast,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier
            .width(80.dp)
            .clip(shape)
            .background(Color.White.copy(alpha = 0.10f))
            .border(1.dp, Color.White.copy(alpha = 0.16f), shape)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = hour.timeLabel,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.58f),
            maxLines = 1,
        )
        Spacer(modifier = Modifier.height(8.dp))
        WeatherGlyph(
            condition = hour.condition,
            isDay = hour.isDay,
            modifier = Modifier.size(36.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.temperature_format, hour.temperatureCelsius),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(3.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            RainDrop(modifier = Modifier.size(width = 6.dp, height = 8.dp))
            Text(
                text = stringResource(
                    R.string.percent_format,
                    hour.precipitationProbabilityPercent,
                ),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.60f),
            )
        }
    }
}

@Preview(name = "Hourly card", showBackground = true, widthDp = 412, heightDp = 240)
@Composable
private fun HourlyCardPreview() {
    GuidelineAppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF203A55))
                .padding(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                HourlyCard(hour = WeatherSampleData.forecast[0].hourly[2])
                HourlyCard(hour = WeatherSampleData.forecast[0].hourly[5])
            }
        }
    }
}
