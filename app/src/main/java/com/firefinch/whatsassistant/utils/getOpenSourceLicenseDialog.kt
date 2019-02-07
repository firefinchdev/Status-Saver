package com.firefinch.whatsassistant.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.webkit.WebView
import android.widget.Button
import com.firefinch.whatsassistant.R

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