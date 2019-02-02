package com.softinit.whatsdirect.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.softinit.whatsdirect.R
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.lang.Exception

fun getDeleteStatusDialog(context: Context, callback: () -> Unit): AlertDialog =
    AlertDialog.Builder(context).let {builder ->
        val btnOk: Button
        val btnCancel: Button
        builder.setView(LayoutInflater.from(context).inflate(R.layout.dialog_delete_status, null)
            .apply {
                btnOk = findViewById(R.id.btn_ok)
                btnCancel = findViewById(R.id.btn_cancel)
            })
//        builder.setTitle("Are you sure you want to delete this status?")
        val dialog = builder.create()
        btnOk.setOnClickListener {
            callback()
            dialog.dismiss()
        }
        btnCancel.setOnClickListener { dialog.dismiss() }
        return dialog
    }