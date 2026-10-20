package com.medioka.guideline.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.medioka.guideline.app.presentation.WeatherApp
import com.medioka.guideline.app.presentation.WeatherSampleData
import com.medioka.guideline.app.theme.GuidelineAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )
        setContent {
            GuidelineAppTheme {
                WeatherApp(
                    // TODO: replace the sample state with the real data source.
                    state = WeatherSampleData.success,
                    onRefresh = {},
                    onRetry = {},
                    onDaySelected = {},
                )
            }
        }
    }
}
