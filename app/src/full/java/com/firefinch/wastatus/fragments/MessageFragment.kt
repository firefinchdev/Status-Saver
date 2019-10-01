package com.firefinch.wastatus.fragments

import android.Manifest
import android.app.Activity
import android.view.View
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firefinch.wastatus.R
import com.firefinch.wastatus.activities.MainActivity
import com.firefinch.wastatus.adapters.CallLogRVAdapter
import com.firefinch.wastatus.extensions.setAdapterWithViewHeight
import com.firefinch.wastatus.interfaces.OnCallLogSelectedListener
import com.firefinch.wastatus.utils.hasPermissions
import com.google.android.material.appbar.AppBarLayout

class MessageFragment: BaseMessageFragment(), OnCallLogSelectedListener {

    private var rvCallLog: RecyclerView? = null
    private lateinit var llPermissionError: LinearLayout
    private lateinit var appbar: AppBarLayout

    private val listItemCallLogHeight = R.dimen.height_item_call_item  //Change when updating list_item_call_log.xml

    override fun setViewIds(view: View) {
        super.setViewIds(view)
        rvCallLog = view.findViewById(R.id.rv_call_log)
        llPermissionError = view.findViewById(R.id.ll_permission_error)
        appbar = view.findViewById(R.id.appbar)
    }

    override fun initiate(view: View) {
        super.initiate(view)
        llPermissionError.setOnClickListener(this)
        if (hasPermissions(context, arrayOf(Manifest.permission.READ_CALL_LOG))) {
            llPermissionError.visibility = View.GONE
            rvCallLog?.layoutManager = LinearLayoutManager(context)
            rvCallLog?.setAdapterWithViewHeight(
                CallLogRVAdapter(context!!, this),
                activity?.resources?.getDimension(listItemCallLogHeight)?.toInt()
            )
//            rvCallLog?.adapter = CallLogRVAdapter(context!!, this)
        } else {
//            ActivityCompat.requestPermissions((activity as Activity), arrayOf(Manifest.permission.READ_CALL_LOG), MainActivity.KEY_REQUEST_PERMISSION)
        }
    }

    override fun getDefaultCallLogCountryCode(): String = getDefaultCountryCode()

    override fun onCallLogSelect(phoneNumber: String, position: Int) {
        setDirectPhoneNumber(phoneNumber)
        appbar.setExpanded(true, true)
        if (position > CALL_LOG_SMOOTH_SCROLL_LIMIT) {
            rvCallLog?.scrollToPosition(CALL_LOG_SMOOTH_SCROLL_LIMIT)
        }
        rvCallLog?.smoothScrollToPosition(0)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.ll_permission_error -> {
                if (!hasPermissions(context, arrayOf(Manifest.permission.READ_CALL_LOG))) {
                    ActivityCompat.requestPermissions((activity as Activity), arrayOf(Manifest.permission.READ_CALL_LOG), MainActivity.KEY_REQUEST_CALL_LOG_PERMISSION)
                }
            }
            else -> return
        }
    }
}