package com.firefinch.whatsassistant.fragments

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
import com.firefinch.whatsassistant.R
import com.firefinch.whatsassistant.activities.MainActivity
import com.firefinch.whatsassistant.adapters.CallLogRVAdapter
import com.firefinch.whatsassistant.interfaces.OnCallLogSelectedListener
import com.firefinch.whatsassistant.utils.hasPermissions
import com.firefinch.whatsassistant.extensions.setAdapterWithViewHeight
import com.firefinch.whatsassistant.extensions.setupClearButtonWithAction
import com.firefinch.whatsassistant.utils.AppIntent
import io.michaelrocks.libphonenumber.android.Phonenumber

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

    private val listItemCallLogHeight = R.dimen.height_item_call_item  //Change when updating list_item_call_log.xml

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

        if (hasPermissions(context, arrayOf(Manifest.permission.READ_CALL_LOG))) {
            llPermissionError.visibility = View.GONE
            rvCallLog?.layoutManager = LinearLayoutManager(context)
            rvCallLog?.setAdapterWithViewHeight(CallLogRVAdapter(context!!, this),
                activity?.resources?.getDimension(listItemCallLogHeight)?.toInt()
            )
//            rvCallLog?.adapter = CallLogRVAdapter(context!!, this)
        } else {
//            ActivityCompat.requestPermissions((activity as Activity), arrayOf(Manifest.permission.READ_CALL_LOG), MainActivity.KEY_REQUEST_PERMISSION)
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

    override fun onCallLogSelect(phoneNumber: Phonenumber.PhoneNumber, postition: Int) {
        spinnerCountry.setCountryForPhoneCode(phoneNumber.countryCode)
        etWhatsAppNum.setText(phoneNumber.nationalNumber.toString())
        appbar.setExpanded(true, true)
        if (postition > CALL_LOG_SMOOTH_SCROLL_LIMIT) {
            rvCallLog?.scrollToPosition(CALL_LOG_SMOOTH_SCROLL_LIMIT)
        }
        rvCallLog?.smoothScrollToPosition(0)
    }

    override fun getDefaultCallLogCountryCode(): String = spinnerCountry.defaultCountryNameCode

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}