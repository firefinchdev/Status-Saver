package com.firefinch.wastatus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import com.google.android.material.appbar.AppBarLayout
import com.hbb20.CountryCodePicker
import com.firefinch.wastatus.R
import com.firefinch.wastatus.extensions.setupClearButtonWithAction
import com.firefinch.wastatus.utils.AppIntent
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context


open class BaseMessageFragment: androidx.fragment.app.Fragment(), View.OnClickListener {

    private lateinit var etWhatsAppNum: EditText
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var spinnerCountry: CountryCodePicker
    private lateinit var cbPreferBusiness: CheckBox
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var appbar: AppBarLayout

    private var directPhoneNum: String? = null



    private lateinit var clipboardManager: ClipboardManager

    companion object {
        const val CALL_LOG_SMOOTH_SCROLL_LIMIT = 30
        fun newInstance(): BaseMessageFragment {
            return BaseMessageFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =  inflater.inflate(R.layout.fragment_message, container, false)
        setViewIds(view)
        initiate(view)
        return view
    }

    open fun setViewIds(view: View) {
        etWhatsAppNum = view.findViewById(R.id.edit_text_whatsapp_no)
        etMessage = view.findViewById(R.id.edit_text_whatsapp_message)
        btnSend = view.findViewById(R.id.btn_send)
        spinnerCountry = view.findViewById(R.id.spinner_country)
        cbPreferBusiness = view.findViewById(R.id.cb_prefer_business)
        coordinatorLayout = view.findViewById(R.id.coordinator_fragment_message)
        appbar = view.findViewById(R.id.appbar)

    }

    open fun initiate(view: View) {
        btnSend.setOnClickListener(this)
        etWhatsAppNum.setupClearButtonWithAction()
        etMessage.setupClearButtonWithAction()

        directPhoneNum?.let {
            setDirectPhoneNumber(it)
            directPhoneNum = null
        }

        clipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        clipboardSetup(view)
        clipboardManager.addPrimaryClipChangedListener { clipboardSetup(view) }
    }

    private fun clipboardSetup(view: View) {

        fun setClipboardLayoutView(visibility: Int) {
            view.findViewById<View>(R.id.ll_clipboard).visibility = visibility
        }

        if (!clipboardManager.hasPrimaryClip()) {
            setClipboardLayoutView(View.GONE)
        } else if (!clipboardManager.primaryClipDescription.hasMimeType(MIMETYPE_TEXT_PLAIN)) {
            // data is not plain text
            setClipboardLayoutView(View.GONE)
        } else {
            //since the clipboard contains plain text.
            val item = clipboardManager.primaryClip.getItemAt(0)

            val phone = item.text.toString()

            // For now, assuming phone num is a string with optional + and length >= 6
            fun String.isPhone() = matches("""^(\+?)\d{6,}$""".toRegex())

            if(phone.isPhone()) {
                setClipboardLayoutView(View.VISIBLE)
                view.findViewById<TextView>(R.id.tv_cb_phone).text = phone
                view.findViewById<Button>(R.id.btn_cb_send).setOnClickListener {
                    val message = etMessage.text.toString()
                    context?.let { it1 -> AppIntent.sendIntent(it1, phone, message,cbPreferBusiness.isChecked) }
                }
            } else {
                setClipboardLayoutView(View.GONE)
            }

        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_send -> {
                val phone = "${spinnerCountry.selectedCountryCode}${etWhatsAppNum.text}"
                val message = etMessage.text.toString()
                AppIntent.sendIntent(context!!, phone, message,cbPreferBusiness.isChecked)
            }
            else -> return
        }
    }



    fun setDirectPhoneNumber(phoneNum: String) {
        val phoneNumberUtil = PhoneNumberUtil.createInstance(context)
        val phoneNumber: Phonenumber.PhoneNumber = phoneNumberUtil.parse(phoneNum, getDefaultCountryCode())
        spinnerCountry.setCountryForPhoneCode(phoneNumber.countryCode)
        etWhatsAppNum.setText(phoneNumber.nationalNumber.toString())
    }

    fun scheduleDirectPhoneNumber(phoneNum: String) {
        directPhoneNum = phoneNum
    }

    fun getDefaultCountryCode(): String = spinnerCountry.defaultCountryNameCode

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}