package com.softinit.whatsdirect.fragments

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
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.activities.MainActivity
import com.softinit.whatsdirect.adapters.StatusRecyclerAdapter
import com.softinit.whatsdirect.utils.DIR_SAVED_STATUS
import com.softinit.whatsdirect.utils.calculateNoOfColumns
import com.softinit.whatsdirect.utils.hasPermissions

class SavedStatusFragment: androidx.fragment.app.Fragment(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private lateinit var rvStatus: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var layoutPermissionError: View

    companion object {
        fun newInstance(): SavedStatusFragment {
            return SavedStatusFragment()
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
    }

    private fun initiate(view: View) {
        layoutPermissionError.setOnClickListener(this)
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.colorPrimary))

        if (hasPermissions(context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))) {
            layoutPermissionError.visibility = View.GONE
            rvStatus.layoutManager = GridLayoutManager(context, calculateNoOfColumns(context!!, 180) //R.dimen.height_status_thumbnail
                .let { if (it > 0) it else 1 })   //Minimum 1
            rvStatus.adapter = StatusRecyclerAdapter((activity as Activity), DIR_SAVED_STATUS, StatusRecyclerAdapter.options().apply {
                allowShare = true
                allowDelete = true
            })
            refreshLayout.setOnRefreshListener(this)
        }
    }

    fun refresh() = onRefresh()

    override fun onRefresh() {
        if(rvStatus.adapter != null) {
            (rvStatus.adapter as StatusRecyclerAdapter).refresh()
        }
        refreshLayout.isRefreshing = false
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.layout_error_permission -> {
                if (!hasPermissions(context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))) {
                    ActivityCompat.requestPermissions((activity as Activity), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MainActivity.KEY_REQUEST_WRITE_EXTERNAL_PERMISSION)
                }
            }
        }
    }

}
