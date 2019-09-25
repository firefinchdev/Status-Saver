package com.firefinch.wastatus.fragments

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.firefinch.wastatus.R
import com.firefinch.wastatus.activities.MainActivity
import com.firefinch.wastatus.adapters.StatusRecyclerAdapter
import com.firefinch.wastatus.utils.DIR_WHATSAPP_STATUS
import com.firefinch.wastatus.utils.calculateNoOfColumns
import com.firefinch.wastatus.utils.hasPermissions

class StatusFragment: androidx.fragment.app.Fragment(),
                SwipeRefreshLayout.OnRefreshListener, View.OnClickListener,
                StatusRecyclerAdapter.OnStatusAdapterActions{

    private var onCreateViewCalled: Boolean = false
    private lateinit var rvStatus: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var layoutPermissionError: View
    private lateinit var layoutNoStatus: View

    companion object {
        fun newInstance(): StatusFragment {
            return StatusFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =  inflater.inflate(R.layout.fragment_status, container, false)
        setViewIds(view)
        initiate(view)
        onCreateViewCalled = true
        return view
    }

    override fun onResume() {
        super.onResume()
        if(rvStatus.adapter != null) {
            (rvStatus.adapter as StatusRecyclerAdapter).refresh()
        }
    }

    private fun setViewIds(view: View) {
        rvStatus = view.findViewById(R.id.rv_statuses)
        refreshLayout = view.findViewById(R.id.swipe_refresh_status)
        layoutPermissionError = view.findViewById(R.id.layout_error_permission)
        layoutNoStatus = view.findViewById(R.id.layout_no_status)
    }

    private fun initiate(view: View) {
        layoutPermissionError.setOnClickListener(this)
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.colorPrimary))
        rvStatus.layoutManager = GridLayoutManager(context, calculateNoOfColumns(context!!, 180) //R.dimen.height_status_thumbnail
            .let { if (it > 0) it else 1 })   //Minimum 1
        rvStatus.adapter = StatusRecyclerAdapter((activity as Activity), DIR_WHATSAPP_STATUS, this, StatusRecyclerAdapter.options().apply {
            allowSave = true
            allowShare = true
        })
        refreshLayout.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        if (!onCreateViewCalled) {
            return
        }
        if(rvStatus.adapter != null) {
            (rvStatus.adapter as StatusRecyclerAdapter).refresh()
        }
        refreshLayout.isRefreshing = false
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.layout_error_permission -> {
                if (!hasPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                    ActivityCompat.requestPermissions((activity as Activity), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MainActivity.KEY_REQUEST_WRITE_EXTERNAL_PERMISSION)
                }
            }
        }
    }

    override fun adapterHasPermissions() {
        layoutPermissionError.visibility = View.GONE
        layoutNoStatus.visibility = View.GONE
    }

    override fun adapterNotHasPermissions() {
        layoutPermissionError.visibility = View.VISIBLE
        layoutNoStatus.visibility = View.GONE
    }

    override fun adapterEmptyStatusDirectory() {
        layoutNoStatus.visibility = View.VISIBLE
    }
}