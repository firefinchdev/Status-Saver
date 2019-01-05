package com.softinit.dialogspinner

import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.appcompat.widget.AppCompatSpinner

class DialogSpinner: AppCompatSpinner {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun setAdapter(adapter: SpinnerAdapter?) {
        super.setAdapter(adapter)
    }
}