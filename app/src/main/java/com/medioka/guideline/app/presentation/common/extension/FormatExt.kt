package com.medioka.guideline.app.presentation.common.extension

import java.util.Locale

fun Double.formatDecimal(decimals: Int = 1): String {
    val rounded = if (decimals == 0) {
        this.toInt().toString()
    } else {
        String.format(Locale.US, "%.${decimals}f", this)
    }
    return if (decimals > 0 && rounded.endsWith(".0")) {
        rounded.dropLast(2)
    } else {
        rounded
    }
}