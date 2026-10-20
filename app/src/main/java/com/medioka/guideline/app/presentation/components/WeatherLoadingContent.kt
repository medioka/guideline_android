package com.medioka.guideline.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.medioka.guideline.app.presentation.LoadingBackdrop
import com.medioka.guideline.app.theme.GuidelineAppTheme

@Composable
fun WeatherLoadingContent(modifier: Modifier = Modifier) {
    WeatherBackground(backdrop = LoadingBackdrop, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .shimmer(CircleShape),
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(width = 96.dp, height = 10.dp)
                            .shimmer(RoundedCornerShape(4.dp)),
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Box(
                        modifier = Modifier
                            .size(width = 150.dp, height = 18.dp)
                            .shimmer(RoundedCornerShape(5.dp)),
                    )
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .shimmer(CircleShape),
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(36.dp))
                Box(
                    modifier = Modifier
                        .size(width = 108.dp, height = 14.dp)
                        .shimmer(RoundedCornerShape(5.dp)),
                )
                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .size(width = 214.dp, height = 82.dp)
                        .shimmer(RoundedCornerShape(22.dp)),
                )
                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .size(width = 132.dp, height = 16.dp)
                        .shimmer(RoundedCornerShape(5.dp)),
                )

                Spacer(modifier = Modifier.height(42.dp))
                Box(
                    modifier = Modifier
                        .size(width = 146.dp, height = 20.dp)
                        .shimmer(RoundedCornerShape(6.dp)),
                )
                Spacer(modifier = Modifier.height(14.dp))
                repeat(3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        repeat(2) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(78.dp)
                                    .shimmer(RoundedCornerShape(18.dp)),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .size(width = 154.dp, height = 20.dp)
                        .shimmer(RoundedCornerShape(6.dp)),
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .size(width = 98.dp, height = 182.dp)
                                .shimmer(RoundedCornerShape(22.dp)),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(34.dp))
            Text(
                text = stringResource(R.string.loading_weather),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.52f),
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(26.dp))
        }
    }
}

@Preview(name = "Loading content", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun WeatherLoadingContentPreview() {
    GuidelineAppTheme(darkTheme = true) {
        WeatherLoadingContent()
    }
}
