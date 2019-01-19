package com.softinit.whatsdirect.viewholders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.adapters.StatusRecyclerAdapter
import java.io.File

class VideoViewHolder: StatusRecyclerAdapter.StatusViewHolder {
    companion object {
        @JvmStatic
        fun inflateView(context: Context, parent: ViewGroup): VideoViewHolder = VideoViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.list_item_video_status, parent, false))
    }

    constructor(view: View): super(view) {

    }
    override fun bindView(file: File) {

    }

    override fun statusType(): Int = StatusRecyclerAdapter.TYPE_VIDEO
}