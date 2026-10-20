package com.medioka.guideline.app.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medioka.guideline.app.R
import com.medioka.guideline.app.presentation.common.previews.WeatherSampleData
import com.medioka.guideline.app.presentation.common.model.CurrentWeather
import com.medioka.guideline.app.theme.GuidelineAppTheme

@Composable
fun LocationBar(
    current: CurrentWeather,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = Color.White.copy(alpha = 0.82f),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.current_location).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.55f),
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = current.locationName,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.updated_at, current.updatedAtLabel),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.55f),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.11f))
                .border(1.dp, Color.White.copy(alpha = 0.18f), CircleShape)
                .clickable(role = Role.Button, onClick = onRefresh),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = stringResource(R.string.refresh_weather),
                modifier = Modifier.size(20.dp),
                tint = Color.White.copy(alpha = 0.92f),
            )
        }
    }
}

@Preview(name = "Location bar", showBackground = true, widthDp = 412, heightDp = 220)
@Composable
private fun LocationBarPreview() {
    GuidelineAppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2F6DBE)),
            contentAlignment = Alignment.Center,
        ) {
            LocationBar(
                current = WeatherSampleData.current,
                onRefresh = {},
            )
        }
    }
}
