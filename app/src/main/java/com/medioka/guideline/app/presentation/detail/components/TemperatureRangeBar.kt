package com.medioka.guideline.app.presentation.detail.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medioka.guideline.app.R
import com.medioka.guideline.app.theme.GuidelineAppTheme

@Composable
fun TemperatureRangeBar(
    lowCelsius: Int,
    highCelsius: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.temperature_range).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.56f),
            )
            Text(
                text = stringResource(
                    R.string.high_low_plain_format,
                    highCelsius,
                    lowCelsius,
                ),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.56f),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF7CC7FF),
                            Color(0xFFFFD166),
                            Color(0xFFFF9A6C),
                        ),
                    ),
                ),
        )
    }
}

@Preview(name = "Temperature range", showBackground = true, widthDp = 412, heightDp = 200)
@Composable
private fun TemperatureRangeBarPreview() {
    GuidelineAppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2F6DBE))
                .padding(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            TemperatureRangeBar(lowCelsius = 13, highCelsius = 21)
        }
    }
}
