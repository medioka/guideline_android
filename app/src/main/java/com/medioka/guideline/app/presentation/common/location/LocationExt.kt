package com.medioka.guideline.app.presentation.common.location

import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat

fun Activity.hasLocationPermission(): Boolean {
    val coarse = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    val fine = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    return coarse == android.content.pm.PackageManager.PERMISSION_GRANTED ||
            fine == android.content.pm.PackageManager.PERMISSION_GRANTED
}
