package com.firefinch.whatsassistant.interfaces

interface OnCallLogSelectedListener {
    fun onCallLogSelect(phoneNumber: String, position: Int)
    fun getDefaultCallLogCountryCode(): String
}