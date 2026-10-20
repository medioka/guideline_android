package com.medioka.guideline.app.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medioka.guideline.app.theme.GuidelineAppTheme

data class WeatherDetail(
    val label: String,
    val value: String,
)

@Composable
fun WeatherDetailsGrid(
    details: List<WeatherDetail>,
    modifier: Modifier = Modifier,
    columns: Int = 2,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        details.chunked(columns).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                rowItems.forEach { detail ->
                    WeatherDetailTile(
                        detail = detail,
                        modifier = Modifier.weight(1f),
                    )
                }
                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun WeatherDetailTile(
    detail: WeatherDetail,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                color = Color.White.copy(alpha = 0.10f),
                shape = RoundedCornerShape(18.dp),
            )
            .padding(horizontal = 15.dp, vertical = 14.dp),
    ) {
        Text(
            text = detail.label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.58f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = detail.value,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(name = "Weather details grid", showBackground = true, widthDp = 412, heightDp = 420)
@Composable
private fun WeatherDetailsGridPreview() {
    GuidelineAppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2F6DBE))
                .padding(20.dp),
        ) {
            WeatherDetailsGrid(
                details = listOf(
                    WeatherDetail(label = "Feels like", value = "17°"),
                    WeatherDetail(label = "Humidity", value = "68%"),
                    WeatherDetail(label = "Wind", value = "14 km/h W"),
                    WeatherDetail(label = "UV index", value = "4"),
                    WeatherDetail(label = "Pressure", value = "1014 hPa"),
                    WeatherDetail(label = "Visibility", value = "16 km"),
                ),
            )
        }
    }
}
