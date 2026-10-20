package com.medioka.guideline.app.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medioka.guideline.app.theme.GuidelineAppTheme

/**
 * Shimmering placeholder block used by the loading state.
 */
@Composable
fun Modifier.shimmer(shape: Shape = RoundedCornerShape(12.dp)): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val offsetX by transition.animateFloat(
        initialValue = -700f,
        targetValue = 1400f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer-offset",
    )

    val base = Color.White.copy(alpha = 0.10f)
    val highlight = Color.White.copy(alpha = 0.28f)

    return background(
        brush = Brush.linearGradient(
            colors = listOf(base, highlight, base),
            start = Offset(offsetX, 0f),
            end = Offset(offsetX + 520f, 520f),
        ),
        shape = shape,
    )
}

@Preview(name = "Shimmer", showBackground = true, widthDp = 412, heightDp = 260)
@Composable
private fun ShimmerPreview() {
    GuidelineAppTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF16233A))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(modifier = Modifier.width(180.dp).height(22.dp).shimmer())
            Box(modifier = Modifier.width(260.dp).height(64.dp).shimmer(RoundedCornerShape(20.dp)))
            Box(modifier = Modifier.fillMaxSize().shimmer(RoundedCornerShape(24.dp)))
        }
    }
}
