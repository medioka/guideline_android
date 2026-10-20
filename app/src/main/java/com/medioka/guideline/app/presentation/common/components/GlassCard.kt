package com.medioka.guideline.app.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medioka.guideline.app.theme.GuidelineAppTheme

/**
 * Frosted card treatment used above the weather backdrop.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    highlighted: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val alpha = if (highlighted) 0.24f else 0.11f
    val borderAlpha = if (highlighted) 0.46f else 0.17f

    Column(
        modifier = modifier
            .clip(shape)
            .background(
                color = Color.White.copy(alpha = alpha),
                shape = shape,
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = borderAlpha),
                shape = shape,
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(role = Role.Button, onClick = onClick)
                } else {
                    Modifier
                },
            )
            .padding(contentPadding),
        verticalArrangement = Arrangement.Top,
        content = content,
    )
}

@Preview(name = "Glass card", showBackground = true, widthDp = 412, heightDp = 280)
@Composable
private fun GlassCardPreview() {
    GuidelineAppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2F6DBE))
                .padding(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            GlassCard(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = "FEELS LIKE",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.58f),
                )
                Text(
                    text = "17°",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                )
            }
        }
    }
}
