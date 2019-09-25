package com.firefinch.wastatus.utils

import android.app.AlertDialog
import android.content.Context
import android.webkit.WebView
import com.firefinch.wastatus.R

fun getOpenSourceDialog(context: Context): AlertDialog =
    AlertDialog.Builder(context, R.style.AppTheme_DialogTheme).apply {
        setTitle("Open Source Licenses")
        setView(WebView(context)
            .apply {
                loadUrl("file:///android_asset/open_source_licenses.html")
            })
        setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }
    }.let {
        return it.create()
    }