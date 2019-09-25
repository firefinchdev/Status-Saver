package com.firefinch.wastatus.utils

import android.content.Context


fun calculateNoOfColumns(context: Context, itemHeight: Int): Int {
    val displayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    return (dpWidth / itemHeight).toInt()
}