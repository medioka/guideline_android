package com.medioka.guideline.app.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.medioka.guideline.app.presentation.common.components.GlassCard
import com.medioka.guideline.app.theme.GuidelineAppTheme

@Composable
fun SunTimesCard(
    sunriseLabel: String,
    sunsetLabel: String,
    modifier: Modifier = Modifier,
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SunTime(
                label = stringResource(R.string.sunrise),
                value = sunriseLabel,
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(34.dp)
                    .background(Color.White.copy(alpha = 0.16f)),
            )
            SunTime(
                label = stringResource(R.string.sunset),
                value = sunsetLabel,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 18.dp),
            )
        }
    }
}

@Composable
private fun SunTime(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.56f),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
        )
    }
}

@Preview(name = "Sun times card", showBackground = true, widthDp = 412, heightDp = 220)
@Composable
private fun SunTimesCardPreview() {
    GuidelineAppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2F6DBE))
                .padding(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            SunTimesCard(
                sunriseLabel = "7:22 AM",
                sunsetLabel = "6:31 PM",
            )
        }
    }
}
