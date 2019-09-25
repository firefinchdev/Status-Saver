package com.firefinch.wastatus.interfaces

interface OnCallLogSelectedListener {
    fun onCallLogSelect(phoneNumber: String, position: Int)
    fun getDefaultCallLogCountryCode(): String
}