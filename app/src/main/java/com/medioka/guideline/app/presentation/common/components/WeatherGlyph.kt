package com.medioka.guideline.app.presentation.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medioka.guideline.app.presentation.common.model.WeatherCondition
import com.medioka.guideline.app.theme.GuidelineAppTheme
import kotlin.math.cos
import kotlin.math.sin

private val SunColor = Color(0xFFFFD166)
private val MoonColor = Color(0xFFDCE8FF)
private val RainColor = Color(0xFF9ED8FF)
private val SnowColor = Color(0xFFEAF6FF)
private val BoltColor = Color(0xFFFFE082)

/**
 * Hand-drawn weather illustration. Keeping the glyphs custom avoids pulling in the
 * very large material-icons-extended artifact for a single visual style.
 */
@Composable
fun WeatherGlyph(
    condition: WeatherCondition,
    isDay: Boolean,
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
) {
    Canvas(modifier) {
        val s = size.minDimension
        val c = center

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(glowColor(condition, isDay).copy(alpha = 0.32f), Color.Transparent),
                center = c,
                radius = s * 0.52f,
            ),
            radius = s * 0.52f,
            center = c,
        )

        when (condition) {
            WeatherCondition.CLEAR -> {
                if (isDay) drawSun(c, s * 0.20f, SunColor) else drawMoon(c, s * 0.23f, MoonColor)
            }

            WeatherCondition.PARTLY_CLOUDY -> {
                val sunCenter = Offset(c.x + s * 0.17f, c.y - s * 0.16f)
                if (isDay) {
                    drawSun(sunCenter, s * 0.14f, SunColor)
                } else {
                    drawMoon(sunCenter, s * 0.16f, MoonColor)
                }
                drawCloud(Offset(c.x - s * 0.04f, c.y + s * 0.06f), s * 0.62f, tint)
            }

            WeatherCondition.CLOUDY -> {
                drawCloud(
                    center = Offset(c.x + s * 0.08f, c.y - s * 0.10f),
                    width = s * 0.52f,
                    color = tint.copy(alpha = 0.45f),
                )
                drawCloud(Offset(c.x - s * 0.06f, c.y + s * 0.05f), s * 0.64f, tint)
            }

            WeatherCondition.FOG -> {
                drawCloud(Offset(c.x, c.y - s * 0.10f), s * 0.62f, tint)
                repeat(3) { index ->
                    val y = c.y + s * (0.23f + index * 0.08f)
                    val inset = s * (0.20f + index * 0.03f)
                    drawLine(
                        color = tint.copy(alpha = 0.85f - index * 0.18f),
                        start = Offset(c.x - s * 0.22f + inset, y),
                        end = Offset(c.x + s * 0.26f - inset, y),
                        strokeWidth = s * 0.032f,
                        cap = StrokeCap.Round,
                    )
                }
            }

            WeatherCondition.DRIZZLE -> {
                drawCloud(Offset(c.x, c.y - s * 0.12f), s * 0.62f, tint)
                repeat(3) { index ->
                    val x = c.x + s * (-0.14f + index * 0.14f)
                    drawLine(
                        color = RainColor,
                        start = Offset(x, c.y + s * 0.18f),
                        end = Offset(x - s * 0.035f, c.y + s * 0.28f),
                        strokeWidth = s * 0.036f,
                        cap = StrokeCap.Round,
                    )
                }
            }

            WeatherCondition.RAIN -> {
                drawCloud(Offset(c.x, c.y - s * 0.14f), s * 0.62f, tint)
                repeat(4) { index ->
                    val x = c.x + s * (-0.18f + index * 0.12f)
                    drawLine(
                        color = RainColor,
                        start = Offset(x, c.y + s * 0.18f),
                        end = Offset(x - s * 0.06f, c.y + s * 0.37f),
                        strokeWidth = s * 0.04f,
                        cap = StrokeCap.Round,
                    )
                }
            }

            WeatherCondition.SNOW -> {
                drawCloud(Offset(c.x, c.y - s * 0.14f), s * 0.62f, tint)
                repeat(3) { index ->
                    drawSnowflake(
                        center = Offset(
                            x = c.x + s * (-0.15f + index * 0.15f),
                            y = c.y + s * (0.25f + (index % 2) * 0.07f),
                        ),
                        radius = s * 0.055f,
                        color = SnowColor,
                    )
                }
            }

            WeatherCondition.THUNDERSTORM -> {
                drawCloud(Offset(c.x, c.y - s * 0.14f), s * 0.62f, tint)
                drawLightning(c, s * 0.6f, BoltColor)
            }
        }
    }
}

private fun glowColor(condition: WeatherCondition, isDay: Boolean): Color = when {
    !isDay -> MoonColor
    condition == WeatherCondition.CLEAR || condition == WeatherCondition.PARTLY_CLOUDY -> SunColor
    condition == WeatherCondition.THUNDERSTORM -> BoltColor
    condition == WeatherCondition.SNOW -> SnowColor
    else -> RainColor
}

private fun DrawScope.drawSun(center: Offset, radius: Float, color: Color) {
    repeat(8) { index ->
        val angle = Math.toRadians((index * 45).toDouble())
        val direction = Offset(cos(angle).toFloat(), sin(angle).toFloat())
        drawLine(
            color = color.copy(alpha = 0.9f),
            start = center + direction * (radius * 1.42f),
            end = center + direction * (radius * 1.95f),
            strokeWidth = radius * 0.20f,
            cap = StrokeCap.Round,
        )
    }
    drawCircle(color = color, radius = radius, center = center)
}

private fun DrawScope.drawMoon(center: Offset, radius: Float, color: Color) {
    drawCircle(color = color, radius = radius, center = center)
    drawCircle(
        color = Color.Black.copy(alpha = 0.08f),
        radius = radius * 0.22f,
        center = center + Offset(radius * 0.32f, -radius * 0.26f),
    )
    drawCircle(
        color = Color.Black.copy(alpha = 0.06f),
        radius = radius * 0.15f,
        center = center + Offset(-radius * 0.36f, radius * 0.22f),
    )
    drawCircle(
        color = Color.Black.copy(alpha = 0.05f),
        radius = radius * 0.11f,
        center = center + Offset(radius * 0.06f, radius * 0.42f),
    )
}

private fun DrawScope.drawCloud(center: Offset, width: Float, color: Color) {
    drawCircle(
        color = color,
        radius = width * 0.20f,
        center = Offset(center.x - width * 0.21f, center.y),
    )
    drawCircle(
        color = color,
        radius = width * 0.27f,
        center = Offset(center.x, center.y - width * 0.11f),
    )
    drawCircle(
        color = color,
        radius = width * 0.17f,
        center = Offset(center.x + width * 0.23f, center.y + width * 0.02f),
    )
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - width * 0.41f, center.y + width * 0.01f),
        size = Size(width * 0.82f, width * 0.21f),
        cornerRadius = CornerRadius(width * 0.105f),
    )
}

private fun DrawScope.drawSnowflake(center: Offset, radius: Float, color: Color) {
    repeat(3) { index ->
        val angle = Math.toRadians(60.0 * index + 30.0)
        val direction = Offset(cos(angle).toFloat(), sin(angle).toFloat())
        drawLine(
            color = color,
            start = center - direction * radius,
            end = center + direction * radius,
            strokeWidth = radius * 0.35f,
            cap = StrokeCap.Round,
        )
    }
}

private fun DrawScope.drawLightning(center: Offset, width: Float, color: Color) {
    val path = Path().apply {
        moveTo(center.x - width * 0.02f, center.y + width * 0.22f)
        lineTo(center.x + width * 0.10f, center.y + width * 0.22f)
        lineTo(center.x + width * 0.00f, center.y + width * 0.42f)
        lineTo(center.x + width * 0.16f, center.y + width * 0.42f)
        lineTo(center.x - width * 0.08f, center.y + width * 0.72f)
        lineTo(center.x + width * 0.01f, center.y + width * 0.50f)
        lineTo(center.x - width * 0.13f, center.y + width * 0.50f)
        close()
    }
    drawPath(path = path, color = color)
}

@Preview(name = "Weather glyphs", showBackground = true, widthDp = 412, heightDp = 420)
@Composable
private fun WeatherGlyphPreview() {
    GuidelineAppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2F6DBE))
                .padding(20.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                WeatherCondition.entries.chunked(4).forEach { conditions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        conditions.forEach { condition ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                WeatherGlyph(
                                    condition = condition,
                                    isDay = true,
                                    modifier = Modifier.size(72.dp),
                                )
                                Text(
                                    text = condition.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.6f),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
