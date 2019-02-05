package com.softinit.whatsdirect.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.Gravity
import android.widget.Toast
import com.softinit.whatsdirect.R
import java.net.URLEncoder


object AppIntent {
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

    fun share(context: Context): Intent = Intent(Intent.ACTION_SEND)
        .apply {
            putExtra(Intent.EXTRA_TEXT, "Hey check out this cool app at: " +
                    "http://play.google.com/store/apps/details?id=${context.packageName} " +
                    "You can now WhatsApp someone without saving the number. " +
                    "You can also save and share WhatsApp statuses.")
            type = "text/plain"
        }

    fun startPlayStore(context: Context) {
        val uri = Uri.parse("market://details?id=" + context.packageName)
        val i = Intent(Intent.ACTION_VIEW, uri)
        i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        try {
            context.startActivity(i)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=${context.packageName}")
                )
            )
        }
    }

    fun devMail(context: Context, title: String? = null, body: String? = null): Intent {
        val i = Intent(Intent.ACTION_SENDTO)
            .apply {
                type = "text/plain"
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("firefinchdev@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, title ?: "Regarding ${context.getString(R.string.app_name)}: " )
                putExtra(Intent.EXTRA_TEXT, body)
            }
        return Intent.createChooser(i, "Send Email...")
    }
}
