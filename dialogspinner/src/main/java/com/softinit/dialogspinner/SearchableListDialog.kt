package com.softinit.dialogspinner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_ARRAY_ITEMS = "ARG_ARRAY_ITEMS"

class SearchableListDialog : AppCompatDialogFragment() {

    private var mItems :ArrayList<SpinnerItem>? = null
    private lateinit var dialogView: View
    private lateinit var rvItemList: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            mItems = (it.getSerializable(ARG_ARRAY_ITEMS) ?: ArrayList<SpinnerItem>()) as ArrayList<SpinnerItem>
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogView = inflater.inflate(R.layout.layout_searchable_dialog, container, false)

        viewManager = LinearLayoutManager(activity)
        rvItemList = dialogView.findViewById<RecyclerView>(R.id.rv_spinner).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = SpinnerRecyclerViewAdapter(listOf(
                object: SpinnerItem {
                    override fun getImageId() = R.drawable.ic_launcher_foreground
                    override fun getText() = "ANDROID"
                }
            ))
        }
        return dialogView
    }

    companion object {
        @JvmStatic
        fun newInstance(items: ArrayList<SpinnerItem>?) = SearchableListDialog().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_ARRAY_ITEMS, items)
            }
        }
    }
}