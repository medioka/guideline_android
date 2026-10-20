package com.medioka.guideline.app.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medioka.guideline.app.theme.GuidelineAppTheme

/** Small water droplet used by forecast precipitation rows. */
@Composable
fun RainDrop(
    modifier: Modifier = Modifier,
    color: Color = Color.White.copy(alpha = 0.75f),
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.5f, 0f)
            cubicTo(w * 0.72f, h * 0.35f, w * 0.86f, h * 0.52f, w * 0.86f, h * 0.68f)
            cubicTo(w * 0.86f, h * 0.86f, w * 0.70f, h, w * 0.5f, h)
            cubicTo(w * 0.30f, h, w * 0.14f, h * 0.86f, w * 0.14f, h * 0.68f)
            cubicTo(w * 0.14f, h * 0.52f, w * 0.28f, h * 0.35f, w * 0.5f, 0f)
            close()
        }
        drawPath(path = path, color = color)
    }
}

@Preview(name = "Rain drop", showBackground = true, widthDp = 412, heightDp = 180)
@Composable
private fun RainDropPreview() {
    GuidelineAppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF203A55)),
            contentAlignment = Alignment.Center,
        ) {
            RainDrop(modifier = Modifier.size(width = 22.dp, height = 28.dp))
        }
    }
}
