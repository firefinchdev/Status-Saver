package com.softinit.whatsdirect.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.Gravity
import android.widget.Toast
import java.net.URLEncoder

fun sendIntent(context: Context, phoneNum: String, msg: String, preferBusiness: Boolean = false) {
    val packageManager: PackageManager? = context.packageManager
    if (packageManager != null) {
        val intent: Intent = Intent(Intent.ACTION_VIEW)
        val url = "https://api.whatsapp.com/send?phone=$phoneNum&text=${URLEncoder.encode(msg, "UTF-8")}"
        try {
            intent.setPackage(getWhatsAppPackage(context, preferBusiness, packageManager))
            intent.data = Uri.parse(url)
            intent.resolveActivity(packageManager)?.let {
                context.startActivity(intent)
            } ?: run {
                Toast.makeText(context, "WhatsApp not found", Toast.LENGTH_SHORT).apply { setGravity(Gravity.TOP, 0, 0) }.show()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(context, "WhatsApp not found", Toast.LENGTH_SHORT).apply { setGravity(Gravity.TOP, 0, 0) }.show()
        }
    }

}