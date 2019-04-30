package com.firefinch.statussaver.utils

import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.firefinch.statussaver.R
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.lang.Exception

fun getRenameStatusDialog(context: Context, srcFile: File): AlertDialog =
    AlertDialog.Builder(context).let {builder ->
        val etName: EditText
        val btnOk: Button
        val btnCancel: Button
        builder.setView(LayoutInflater.from(context).inflate(R.layout.dialog_rename_status, null)
            .apply {
                etName = findViewById(R.id.et_name)
                btnOk = findViewById(R.id.btn_ok)
                btnCancel = findViewById(R.id.btn_cancel)
            })
        builder.setTitle("Enter Status Name")
        val dialog = builder.create()
        btnOk.setOnClickListener {
            val newName = etName.text.toString()
            if (TextUtils.isEmpty(newName)) {
                Toast.makeText(context, "Invalid Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val extension = FilenameUtils.getExtension(srcFile.name)
            val destFile = File(srcFile.parentFile, etName.text.toString() + "." + extension)
            try {
                FileUtils.moveFile(srcFile, destFile)
                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        btnCancel.setOnClickListener { dialog.dismiss() }
        return dialog
    }