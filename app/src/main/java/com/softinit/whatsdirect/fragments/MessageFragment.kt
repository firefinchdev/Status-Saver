package com.softinit.whatsdirect.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softinit.dialogspinner.DialogSpinner
import com.softinit.dialogspinner.DialogSpinnerAdapter
import com.softinit.dialogspinner.SpinnerItem
import com.softinit.whatsdirect.R

class MessageFragment: androidx.fragment.app.Fragment() {
    companion object {
        fun newInstance(): MessageFragment {
            return MessageFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =  inflater.inflate(R.layout.fragment_message, container, false)

//        view.findViewById<DialogSpinner>(R.id.dialog_spinner).apply {
//            setAdapter(DialogSpinnerAdapter(context, arrayListOf(
//                object: SpinnerItem {
//                    override fun getImageId() = R.drawable.ic_launcher_foreground
//                    override fun getText() = "ANDROID"
//                }
//            )))
//        }
        return view
    }
}