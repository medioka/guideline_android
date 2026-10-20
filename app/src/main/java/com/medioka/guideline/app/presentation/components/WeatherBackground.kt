package com.medioka.guideline.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medioka.guideline.app.presentation.WeatherBackdrop
import com.medioka.guideline.app.theme.GuidelineAppTheme
import com.medioka.guideline.app.theme.WeatherContent

/**
 * Full-screen gradient with two soft light blooms. All weather screens sit on
 * top of this so the white content palette is consistent everywhere.
 */
@Composable
fun WeatherBackground(
    backdrop: WeatherBackdrop,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(backdrop.top, backdrop.bottom),
                ),
            ),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 108.dp, y = (-140).dp)
                .size(380.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(backdrop.glow.copy(alpha = 0.42f), Color.Transparent),
                    ),
                    shape = CircleShape,
                ),
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-160).dp, y = 150.dp)
                .size(360.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(backdrop.glow.copy(alpha = 0.16f), Color.Transparent),
                    ),
                    shape = CircleShape,
                ),
        )
        CompositionLocalProvider(LocalContentColor provides WeatherContent) {
            content()
        }
    }
}

@Preview(name = "Weather background", showBackground = true, widthDp = 412, heightDp = 600)
@Composable
private fun WeatherBackgroundPreview() {
    GuidelineAppTheme(darkTheme = true) {
        WeatherBackground(
            backdrop = WeatherBackdrop(
                top = Color(0xFF2F6DBE),
                bottom = Color(0xFF78A9DC),
                glow = Color(0xFFFFD98A),
            ),
        ) {
            Text(
                text = "Weather background",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(20.dp),
            )
        }
    }
}
