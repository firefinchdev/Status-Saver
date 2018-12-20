package com.softinit.whatsdirect.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softinit.whatsdirect.R
import java.util.zip.Inflater

class IntentFragment: Fragment() {
    companion object {
        fun newInstance(): IntentFragment {
            return IntentFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_fragment_intent, container, false)
    }
}