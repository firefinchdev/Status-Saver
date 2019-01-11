package com.softinit.whatsdirect.utils

import android.content.Context
import android.content.pm.PackageManager

fun getWhatsAppPackage(context: Context?, preferBusiness: Boolean = false, _packageManager: PackageManager?): String {
    val pm = _packageManager ?: context?.packageManager
    val waBusiness = "com.whatsapp.w4b"
    val wa = "com.whatsapp"
    return pm?.let l@{
        if (preferBusiness) {
            try {
                pm.getPackageInfo(waBusiness, 0)
                return@l waBusiness
            } catch (e: PackageManager.NameNotFoundException) {

            }
        }
        try {
            pm.getPackageInfo(wa, 0)
            return@l wa
        } catch (e: PackageManager.NameNotFoundException) {
            throw PackageManager.NameNotFoundException("WhatsApp Not Found")
        }
    } ?: throw Error("Package Manager is null")
}