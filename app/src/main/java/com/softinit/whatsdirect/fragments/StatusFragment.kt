package com.softinit.whatsdirect.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.adapters.StatusRecyclerAdapter
import com.softinit.whatsdirect.utils.calculateNoOfColumns

class StatusFragment: androidx.fragment.app.Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var rvStatus: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout

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
        return view
    }

    override fun onResume() {
        super.onResume()
        (rvStatus.adapter as StatusRecyclerAdapter).refresh()
    }

    private fun setViewIds(view: View) {
        rvStatus = view.findViewById(R.id.rv_statuses)
        refreshLayout = view.findViewById(R.id.swipe_refresh_status)
    }

    private fun initiate(view: View) {
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.colorPrimary))
        rvStatus.layoutManager = GridLayoutManager(context, calculateNoOfColumns(context!!, 180) //R.dimen.height_status_thumbnail
                                        .let { if (it > 0) it else 1 })   //Minimum 1
        rvStatus.adapter = StatusRecyclerAdapter((activity as Activity), StatusRecyclerAdapter.WHATSAPP_DIR)
        refreshLayout.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        (rvStatus.adapter as StatusRecyclerAdapter).refresh()
        refreshLayout.isRefreshing = false
    }

}
