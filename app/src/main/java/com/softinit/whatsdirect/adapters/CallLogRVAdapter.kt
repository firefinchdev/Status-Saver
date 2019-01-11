package com.softinit.whatsdirect.adapters

import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.utils.getCallLogCursor
import kotlinx.android.synthetic.main.fragment_message.view.*

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
//import com.softinit.whatsdirect.R
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
//    class CallLogViewHolder: RecyclerView.ViewHolder {
//        val ivStartIcon: ImageView
//        val tvPhoneNum: TextView
//
//        constructor(view: View): super(view) {
//            ivStartIcon = view.findViewById(R.id.iv_start_icon)
//            tvPhoneNum = view.findViewById(R.id.tv_phone_number)
//        }
//    }
//}

class CallLogRVAdapter : CursorRecyclerViewAdapter<CallLogRVAdapter.CallLogViewHolder> {
    override fun onBindViewHolder(viewHolder: CallLogViewHolder?, cursor: Cursor?) {
        val colNumber: Int? = cursor?.getColumnIndexOrThrow(CallLog.Calls.NUMBER)
        viewHolder?.tvPhoneNum?.text = cursor?.getString(colNumber!!)
        Log.d("MEOW", "MEW")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item_call_log, parent, false)
        return CallLogViewHolder(view)
    }

    override fun getItemCount(): Int {
        val count = super.getItemCount()
        return if (count < 25) count else 25
    }

    //    override fun onBindViewHolderCursor(holder: CallLogViewHolder?, cursor: Cursor?) {
//        val colNumber: Int? = cursor?.getColumnIndexOrThrow(CallLog.Calls.NUMBER)
//        holder?.tvPhoneNum?.text = cursor?.getString(colNumber!!)
//    }

    var context: Context

    constructor(_context: Context): super(_context, getCallLogCursor(_context)) {
        context = _context
    }

    class CallLogViewHolder: RecyclerView.ViewHolder {
        val ivStartIcon: ImageView
        val tvPhoneNum: TextView

        constructor(view: View): super(view) {
            ivStartIcon = view.findViewById(R.id.iv_start_icon)
            tvPhoneNum = view.findViewById(R.id.tv_phone_number)
        }
    }
}