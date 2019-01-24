package com.softinit.whatsdirect.viewholders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.adapters.StatusRecyclerAdapter
import java.io.File

class VideoViewHolder: StatusRecyclerAdapter.StatusViewHolder {
    companion object {
        @JvmStatic
        fun inflateView(context: Context, parent: ViewGroup): VideoViewHolder = VideoViewHolder(context, LayoutInflater.from(context)
                .inflate(R.layout.list_item_video_status, parent, false))
    }

    val context: Context
    val ivStatus: ImageView

    constructor(context: Context, view: View): super(view) {
        this.context = context
        ivStatus = view.findViewById(R.id.iv_status_video)
    }
    override fun bindView(file: File) {
        Glide.with(context)
            .load(file)
            .into(ivStatus)
    }

    override fun statusType(): Int = StatusRecyclerAdapter.TYPE_VIDEO
}