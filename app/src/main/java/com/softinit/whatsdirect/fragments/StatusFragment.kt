package com.softinit.whatsdirect.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.adapters.StatusRecyclerAdapter
import com.softinit.whatsdirect.utils.calculateNoOfColumns

class StatusFragment: androidx.fragment.app.Fragment() {

    private lateinit var rvStatus: RecyclerView

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

    private fun setViewIds(view: View) {
        rvStatus = view.findViewById(R.id.rv_statuses)
    }

    private fun initiate(view: View) {
        rvStatus.layoutManager = GridLayoutManager(context, calculateNoOfColumns(context!!, 180) //R.dimen.height_status_thumbnail
                                        .let { if (it > 0) it else 1 })   //Minimum 1
        rvStatus.adapter = StatusRecyclerAdapter(context!!, StatusRecyclerAdapter.WHATSAPP_DIR)
    }

}