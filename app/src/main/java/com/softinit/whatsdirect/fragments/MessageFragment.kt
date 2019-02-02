package com.softinit.whatsdirect.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
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
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.activities.MainActivity
import com.softinit.whatsdirect.adapters.CallLogRVAdapter
import com.softinit.whatsdirect.interfaces.OnCallLogSelectedListener
import com.softinit.whatsdirect.utils.getWhatsAppPackage
import com.softinit.whatsdirect.utils.hasPermissions
import com.softinit.whatsdirect.utils.setAdapterWithViewHeight
import io.michaelrocks.libphonenumber.android.Phonenumber
import java.net.URLEncoder

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
            R.id.btn_send -> sendIntent(cbPreferBusiness.isChecked)
            R.id.ll_permission_error -> {
                if (!hasPermissions(context, arrayOf(Manifest.permission.READ_CALL_LOG))) {
                    ActivityCompat.requestPermissions((activity as Activity), arrayOf(Manifest.permission.READ_CALL_LOG), MainActivity.KEY_REQUEST_CALL_LOG_PERMISSION)
                }
            }
            else -> return
        }
    }

    private fun sendIntent(preferBusiness: Boolean = false) {
        val packageManager: PackageManager? = context?.packageManager
        if (packageManager != null) {
            val phone = "${spinnerCountry.selectedCountryCode}${etWhatsAppNum.text}"
            val message = etMessage.text.toString()
            val intent: Intent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$phone&text=${URLEncoder.encode(message, "UTF-8")}"
            try {
                intent.setPackage(getWhatsAppPackage(context, preferBusiness, packageManager))
                intent.data = Uri.parse(url)
                intent.resolveActivity(packageManager)?.let {
                    context?.startActivity(intent)
                } ?: run {
                    Toast.makeText(context, "WhatsApp not found", Toast.LENGTH_SHORT).apply { setGravity(Gravity.TOP, 0, 0) }.show()
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(context, "WhatsApp not found", Toast.LENGTH_SHORT).apply { setGravity(Gravity.TOP, 0, 0) }.show()
            }
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