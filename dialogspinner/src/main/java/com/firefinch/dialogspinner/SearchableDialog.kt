package com.firefinch.dialogspinner

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_ARRAY_ITEMS = "ARG_ARRAY_ITEMS"

class SearchableDialog : AppCompatDialogFragment, TextWatcher {

    constructor():super()               //This is so that no one can create instance by calling constructor

    private var mItems :ArrayList<SpinnerItem>? = null
    private lateinit var dialogView: View
    private lateinit var rvItemList: RecyclerView
    private lateinit var etSearch: EditText

//    public var adapter: SpinnerRecyclerViewAdapter
//        get() = rvItemList.adapter as SpinnerRecyclerViewAdapter
//        set(newAdapter) {
//            rvItemList.adapter = newAdapter
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            mItems = (it.getSerializable(ARG_ARRAY_ITEMS) ?: ArrayList<SpinnerItem>()) as ArrayList<SpinnerItem>
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogView = inflater.inflate(R.layout.layout_searchable_dialog, container, false)

        rvItemList = dialogView.findViewById<RecyclerView>(R.id.rv_searchable_dialog).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = SpinnerRecyclerViewAdapter(mItems?.toList()?: emptyList())
        }
        etSearch = dialogView.findViewById<EditText>(R.id.et_searchable_dialog).apply { addTextChangedListener(this@SearchableDialog) }
        return dialogView
    }

    override fun afterTextChanged(s: Editable?) { }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        (rvItemList.adapter as SpinnerRecyclerViewAdapter).filter(s)
    }

//    fun setAdapter(adapter: SpinnerRecyclerViewAdapter) {
//        rvItemList.adapter = adapter
//    }

    companion object {
        @JvmStatic
        fun newInstance(items: ArrayList<SpinnerItem>?) = SearchableDialog().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_ARRAY_ITEMS, items)
            }
        }
    }
}