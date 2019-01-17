package com.softinit.whatsdirect.interfaces

import io.michaelrocks.libphonenumber.android.Phonenumber

interface OnCallLogSelectedListener {
    fun onCallLogSelect(phoneNumber: Phonenumber.PhoneNumber)
    fun getDefaultCallLogCountryCode(): String
}