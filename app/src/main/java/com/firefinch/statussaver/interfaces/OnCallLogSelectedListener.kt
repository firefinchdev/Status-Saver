package com.firefinch.statussaver.interfaces

interface OnCallLogSelectedListener {
    fun onCallLogSelect(phoneNumber: String, position: Int)
    fun getDefaultCallLogCountryCode(): String
}