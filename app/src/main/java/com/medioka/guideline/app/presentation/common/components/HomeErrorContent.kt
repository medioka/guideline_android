package com.medioka.guideline.app.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medioka.guideline.app.R
import com.medioka.guideline.app.presentation.ErrorBackdrop
import com.medioka.guideline.app.presentation.common.model.WeatherErrorReason
import com.medioka.guideline.app.presentation.common.model.WeatherUiState
import com.medioka.guideline.app.theme.GuidelineAppTheme

@Composable
fun HomeErrorContent(
    modifier: Modifier = Modifier,
    error: WeatherUiState.Error,
    onRetry: () -> Unit,
    onOpenSettings: () -> Unit = {}
) {
    val title = when (error.reason) {
        WeatherErrorReason.LOCATION -> stringResource(R.string.error_location_title)
        WeatherErrorReason.NETWORK -> stringResource(R.string.error_network_title)
    }
    val message = when (error.reason) {
        WeatherErrorReason.LOCATION -> stringResource(R.string.error_location_message)
        WeatherErrorReason.NETWORK -> stringResource(R.string.error_network_message)
    }

    WeatherBackground(backdrop = ErrorBackdrop, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(Color.White.copy(alpha = 0.10f), CircleShape)
                    .border(1.dp, Color.White.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = when (error.reason) {
                        WeatherErrorReason.LOCATION -> Icons.Filled.LocationOn
                        WeatherErrorReason.NETWORK -> Icons.Filled.Warning
                    },
                    contentDescription = null,
                    modifier = Modifier.size(42.dp),
                    tint = Color.White.copy(alpha = 0.92f),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.70f),
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 320.dp),
            )
            Spacer(modifier = Modifier.height(28.dp))
            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = ErrorBackdrop.top,
                ),
                contentPadding = PaddingValues(horizontal = 26.dp, vertical = 14.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.try_again),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            if (error.reason == WeatherErrorReason.LOCATION) {
                TextButton(onClick = onOpenSettings) {
                    Text(
                        text = stringResource(R.string.open_settings),
                        color = Color.White.copy(alpha = 0.86f),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
            Text(
                text = stringResource(R.string.powered_by),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.36f),
            )
        }
    }
}

@Preview(name = "Location error", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HomeLocationErrorPreview() {
    GuidelineAppTheme(darkTheme = true) {
        HomeErrorContent(
            error = WeatherUiState.Error(WeatherErrorReason.LOCATION),
            onRetry = {},
        )
    }
}

@Preview(name = "Network error", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HomeNetworkErrorPreview() {
    GuidelineAppTheme(darkTheme = true) {
        HomeErrorContent(
            error = WeatherUiState.Error(WeatherErrorReason.NETWORK),
            onRetry = {},
        )
    }
}
