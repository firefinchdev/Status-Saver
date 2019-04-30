package com.firefinch.statussaver.utils

import android.os.Environment
import java.io.File

private val extStorage = Environment.getExternalStorageDirectory()

val DIR_WHATSAPP_STATUS: File = File(extStorage, "WhatsApp/Media/.Statuses")    //TODO: Fix FC when this directory is not available
//val DIR_WHATSAPP_STATUS: File = File(extStorage, "WhatsApp/Media/.status_copy")
val DIR_SAVED_STATUS: File = File(extStorage, "WhatsApp Saved Statuses")