package com.softinit.whatsdirect.extensions

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setAdapterWithViewHeight(adapter: RecyclerView.Adapter<*>, itemLength: Int?) {
    this.adapter = adapter
    itemLength ?.let { this.layoutParams.height = adapter.itemCount * itemLength }
}