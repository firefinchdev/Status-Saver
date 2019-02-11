package com.firefinch.whatsassistant.adapters

import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firefinch.whatsassistant.R
import com.firefinch.whatsassistant.interfaces.OnCallLogSelectedListener
import com.firefinch.whatsassistant.utils.getCallLogCursor
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import java.text.SimpleDateFormat
import java.util.*

class CallLogRVAdapter : CursorRecyclerViewAdapter<CallLogRVAdapter.CallLogViewHolder> {
    companion object {
        const val layoutResId: Int = R.layout.list_item_call_log
    }
    private var context: Context
    private var callLogSelectedListener: OnCallLogSelectedListener?

    constructor(_context: Context, _callLogSelectedListener: OnCallLogSelectedListener?): super(_context, getCallLogCursor(_context)) {
        context = _context
        callLogSelectedListener = _callLogSelectedListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val view: View = LayoutInflater.from(context).inflate(layoutResId, parent, false)
        return CallLogViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: CallLogViewHolder?, cursor: Cursor?) {
        viewHolder?.bindViewWithCursor(cursor!!)
    }

    fun setOnCallLogSelectedListener(listener: OnCallLogSelectedListener) {
        callLogSelectedListener = listener
    }

    inner class CallLogViewHolder: RecyclerView.ViewHolder {
        val ivStartIcon: ImageView
        val ivArrowCall: ImageView
        val tvPhoneNum: TextView
        val tvDate: TextView
        val tvTime: TextView
        val llRoot: LinearLayout

        constructor(view: View): super(view) {
            ivStartIcon = view.findViewById(R.id.iv_start_icon)
            ivArrowCall = view.findViewById(R.id.iv_arrow_call)
            tvPhoneNum = view.findViewById(R.id.tv_phone_number)
            llRoot = view.findViewById(R.id.ll_root_call_log)
            tvDate = view.findViewById(R.id.tv_call_date)
            tvTime = view.findViewById(R.id.tv_call_time)
        }

        fun bindViewWithCursor(cursor: Cursor) {
            val COL_NUMBER = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val COL_DATE = cursor.getColumnIndex(CallLog.Calls.DATE)
            val COL_TYPE = cursor.getColumnIndex(CallLog.Calls.TYPE)

            val phoneNum = cursor.getString(COL_NUMBER)
            val dateTime = Date(cursor.getString(COL_DATE).toLong())

            SimpleDateFormat("dd/MM,hh:mm a").format(dateTime).split(',').let {
                tvDate.text = it[0]
                tvTime.text = it[1]
            }
            tvPhoneNum.text = phoneNum
            ivArrowCall.setImageResource(when(cursor.getString(COL_TYPE).toInt()) {
                CallLog.Calls.OUTGOING_TYPE -> R.drawable.arrow_outgoing
                CallLog.Calls.INCOMING_TYPE -> R.drawable.arrow_incoming
                CallLog.Calls.MISSED_TYPE -> R.drawable.arrow_missed
                else -> R.drawable.arrow_outgoing
            })

            llRoot.setOnClickListener {
                val phoneNumberUtil = PhoneNumberUtil.createInstance(context)
                val phoneNumber: Phonenumber.PhoneNumber = phoneNumberUtil.parse(phoneNum, callLogSelectedListener?.getDefaultCallLogCountryCode())
                callLogSelectedListener?.onCallLogSelect(phoneNumber, adapterPosition)
            }
        }
    }
}