package com.firefinch.statussaver.adapters

import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.firefinch.statussaver.R
import com.firefinch.statussaver.utils.getCallLogCursor

//
//import android.content.Context
//import android.database.Cursor
//import android.provider.CallLog
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.firefinch.statussaver.R
//
//class CallLogAdapter(val context: Context): RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>() {
//
//    val cursor: Cursor
//    init {
//        cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI,
//            null, null, null, CallLog.Calls.DATE + " DESC")
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
//        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item_call_log, parent, false)
//        return CallLogViewHolder(view)
//    }
//
//    override fun getItemCount(): Int = cursor.count
//
//    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
//
//    }
//
//
//    class CallLogViewHolder: RecyclerView.StatusViewHolder {
//        val ivStartIcon: ImageView
//        val tvPhoneNum: TextView
//
//        constructor(view: View): super(view) {
//            ivStartIcon = view.findViewById(R.id.iv_start_icon)
//            tvPhoneNum = view.findViewById(R.id.tv_phone_number)
//        }
//    }
//}

class CallLogAdapter(context: Context) : CursorAdapter(context, getCallLogCursor(context), 0) {

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.list_item_call_log, parent, false)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val tvPhoneNumber: TextView? = view?.findViewById(R.id.tv_phone_number)
        val colNumber: Int? = cursor?.getColumnIndexOrThrow(CallLog.Calls.NUMBER)
        tvPhoneNumber?.text = cursor?.getString(colNumber!!)
    }
}