package com.firefinch.wastatus.fragments

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.hbb20.CountryCodePicker
import com.firefinch.wastatus.R
import com.firefinch.wastatus.activities.MainActivity
import com.firefinch.wastatus.interfaces.OnCallLogSelectedListener
import com.firefinch.wastatus.utils.hasPermissions
import com.firefinch.wastatus.extensions.setAdapterWithViewHeight
import com.firefinch.wastatus.extensions.setupClearButtonWithAction
import com.firefinch.wastatus.utils.AppIntent
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context


class MessageFragment: androidx.fragment.app.Fragment(), View.OnClickListener, OnCallLogSelectedListener {

    private lateinit var etWhatsAppNum: EditText
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var spinnerCountry: CountryCodePicker
    private lateinit var cbPreferBusiness: CheckBox
    private var rvCallLog: RecyclerView? = null
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var appbar: AppBarLayout
    private lateinit var llPermissionError: LinearLayout
    private var directPhoneNum: String? = null

    private val listItemCallLogHeight = R.dimen.height_item_call_item  //Change when updating list_item_call_log.xml

    private lateinit var clipboardManager: ClipboardManager

    companion object {
        const val CALL_LOG_SMOOTH_SCROLL_LIMIT = 30
        fun newInstance(): MessageFragment {
            return MessageFragment()
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

    private fun setViewIds(view: View) {
        etWhatsAppNum = view.findViewById(R.id.edit_text_whatsapp_no)
        etMessage = view.findViewById(R.id.edit_text_whatsapp_message)
        btnSend = view.findViewById(R.id.btn_send)
        spinnerCountry = view.findViewById(R.id.spinner_country)
        cbPreferBusiness = view.findViewById(R.id.cb_prefer_business)
        rvCallLog = view.findViewById(R.id.rv_call_log)
        coordinatorLayout = view.findViewById(R.id.coordinator_fragment_message)
//        nestedScrollView = view.findViewById(R.id.nested_scroll_view)
        appbar = view.findViewById(R.id.appbar)
        llPermissionError = view.findViewById(R.id.ll_permission_error)
    }

    private fun initiate(view: View) {
        btnSend.setOnClickListener(this)
        etWhatsAppNum.setupClearButtonWithAction()
        etMessage.setupClearButtonWithAction()
        llPermissionError.setOnClickListener(this)
        directPhoneNum?.let {
            setDirectPhoneNumber(it)
            directPhoneNum = null
        }

            // TODO: Fix this
///*
//        if (hasPermissions(context, arrayOf(Manifest.permission.READ_CALL_LOG))) {
//            llPermissionError.visibility = View.GONE
//            rvCallLog?.layoutManager = LinearLayoutManager(context)
//            rvCallLog?.setAdapterWithViewHeight(CallLogRVAdapter(context!!, this),
//                activity?.resources?.getDimension(listItemCallLogHeight)?.toInt()
//            )
////            rvCallLog?.adapter = CallLogRVAdapter(context!!, this)
//        } else {
////            ActivityCompat.requestPermissions((activity as Activity), arrayOf(Manifest.permission.READ_CALL_LOG), MainActivity.KEY_REQUEST_PERMISSION)
//        }
//*/

        // Disable call log view for now
        view.findViewById<View>(R.id.cardViewCallLogs).visibility = View.GONE

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
            R.id.ll_permission_error -> {
                if (!hasPermissions(context, arrayOf(Manifest.permission.READ_CALL_LOG))) {
                    ActivityCompat.requestPermissions((activity as Activity), arrayOf(Manifest.permission.READ_CALL_LOG), MainActivity.KEY_REQUEST_CALL_LOG_PERMISSION)
                }
            }
            else -> return
        }
    }

    override fun onCallLogSelect(phoneNumber: String, position: Int) {
        setDirectPhoneNumber(phoneNumber)
        appbar.setExpanded(true, true)
        if (position > CALL_LOG_SMOOTH_SCROLL_LIMIT) {
            rvCallLog?.scrollToPosition(CALL_LOG_SMOOTH_SCROLL_LIMIT)
        }
        rvCallLog?.smoothScrollToPosition(0)
    }

    fun setDirectPhoneNumber(phoneNum: String) {
        val phoneNumberUtil = PhoneNumberUtil.createInstance(context)
        val phoneNumber: Phonenumber.PhoneNumber = phoneNumberUtil.parse(phoneNum, getDefaultCallLogCountryCode())
        spinnerCountry.setCountryForPhoneCode(phoneNumber.countryCode)
        etWhatsAppNum.setText(phoneNumber.nationalNumber.toString())
    }

    fun scheduleDirectPhoneNumber(phoneNum: String) {
        directPhoneNum = phoneNum
    }

    override fun getDefaultCallLogCountryCode(): String = spinnerCountry.defaultCountryNameCode

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}