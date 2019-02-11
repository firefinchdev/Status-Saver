package com.firefinch.whatsassistant.utils

import android.content.Context
import android.provider.CallLog

fun getCallLogCursor(context: Context) = context.contentResolver.query(
    CallLog.Calls.CONTENT_URI, null, null,
    null, CallLog.Calls.DATE + " DESC")