package com.medioka.guideline.app.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medioka.guideline.app.presentation.common.previews.WeatherSampleData
import com.medioka.guideline.app.presentation.common.components.RainDrop
import com.medioka.guideline.app.presentation.common.components.WeatherGlyph
import com.medioka.guideline.app.presentation.common.model.DailyForecast
import com.medioka.guideline.app.theme.GuidelineAppTheme

@Composable
fun ForecastCard(
    day: DailyForecast,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
) {
    val shape = RoundedCornerShape(22.dp)
    Column(
        modifier = modifier
            .width(98.dp)
            .height(182.dp)
            .clip(shape)
            .background(Color.White.copy(alpha = if (highlighted) 0.22f else 0.10f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = if (highlighted) 0.44f else 0.16f),
                shape = shape,
            )
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = day.dayLabel,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
            maxLines = 1,
        )
        Text(
            text = day.dateLabel,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.58f),
            maxLines = 1,
        )
        Spacer(modifier = Modifier.height(10.dp))
        WeatherGlyph(
            condition = day.condition,
            isDay = true,
            modifier = Modifier.size(44.dp),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                text = "${day.highCelsius}°",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
            Text(
                text = "${day.lowCelsius}°",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.56f),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            RainDrop(modifier = Modifier.size(width = 7.dp, height = 9.dp))
            Text(
                text = "${day.precipitationProbabilityPercent}%",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.66f),
            )
        }
    }
}

@Preview(name = "Forecast card", showBackground = true, widthDp = 412, heightDp = 300)
@Composable
private fun ForecastCardPreview() {
    GuidelineAppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2F6DBE))
                .padding(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            ForecastCard(
                day = WeatherSampleData.forecast.first(),
                onClick = {},
                highlighted = true,
            )
        }
    }
}
