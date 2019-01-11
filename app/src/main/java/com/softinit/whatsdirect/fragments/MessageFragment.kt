package com.softinit.whatsdirect.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.CountryCodePicker
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.adapters.CallLogAdapter
import com.softinit.whatsdirect.adapters.CallLogRVAdapter
import com.softinit.whatsdirect.interfaces.OnCallLogSelectedListener
import com.softinit.whatsdirect.utils.getWhatsAppPackage
import java.net.URLEncoder

class MessageFragment: androidx.fragment.app.Fragment(), View.OnClickListener, OnCallLogSelectedListener {

    private lateinit var etWhatsAppNum: EditText
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var spinnerCountry: CountryCodePicker
    private lateinit var cbPreferBusiness: CheckBox
    private lateinit var listViewCallLog: RecyclerView

    companion object {
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
        listViewCallLog = view.findViewById(R.id.list_view_call_log)
    }

    private fun initiate(view: View) {
        btnSend.setOnClickListener(this)
        listViewCallLog.layoutManager = LinearLayoutManager(context)
        listViewCallLog.adapter = CallLogRVAdapter(context!!, this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_send -> sendIntent(cbPreferBusiness.isChecked)
            else -> return
        }
    }

    private fun sendIntent(preferBusiness: Boolean = false) {
        val packageManager: PackageManager? = context?.packageManager
        if (packageManager != null) {
            val phone = etWhatsAppNum.text.toString()
            val message = etMessage.text.toString()
            val intent: Intent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$phone&text=${URLEncoder.encode(message, "UTF-8")}"
            intent.setPackage(getWhatsAppPackage(context, preferBusiness, packageManager))
            intent.data = Uri.parse(url)
            intent.resolveActivity(packageManager)?.let {
                context?.startActivity(intent);
            } ?: run {
                Toast.makeText(context, "WhatsApp not found", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCallLogSelect(phoneNumber: String) {
        Toast.makeText(context, phoneNumber, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}