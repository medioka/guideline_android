package com.medioka.guideline.app.presentation

import java.util.Locale

fun formatDecimal(value: Double, decimals: Int = 1): String {
    val rounded = if (decimals == 0) {
        value.toInt().toString()
    } else {
        String.format(Locale.US, "%.${decimals}f", value)
    }
    return if (decimals > 0 && rounded.endsWith(".0")) {
        rounded.dropLast(2)
    } else {
        rounded
    }
}
