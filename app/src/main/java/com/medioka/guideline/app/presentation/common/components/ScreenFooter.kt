package com.medioka.guideline.app.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medioka.guideline.app.theme.GuidelineAppTheme

@Composable
fun ScreenFooter(
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.42f),
        )
    }
}

@Preview(name = "Screen footer", showBackground = true, widthDp = 412, heightDp = 180)
@Composable
private fun ScreenFooterPreview() {
    GuidelineAppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2F6DBE)),
            contentAlignment = Alignment.TopCenter,
        ) {
            ScreenFooter(
                text = "Weather data by Open-Meteo",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
