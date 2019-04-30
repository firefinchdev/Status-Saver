package com.firefinch.statussaver.viewholders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firefinch.statussaver.GlideApp
import com.firefinch.statussaver.R
import com.firefinch.statussaver.adapters.StatusRecyclerAdapter
import com.firefinch.statussaver.utils.FileType
import java.io.File

class VideoViewHolder: StatusRecyclerAdapter.StatusViewHolder {
    companion object {
        @JvmStatic
        fun inflateView(context: Context, parent: ViewGroup): VideoViewHolder = VideoViewHolder(context, LayoutInflater.from(context)
                .inflate(R.layout.list_item_video_status, parent, false))
    }

    val context: Context
    val view: View
    val ivStatus: ImageView

    constructor(context: Context, _view: View): super(_view) {
        this.context = context
        this.view = _view
        ivStatus = view.findViewById(R.id.iv_status_video)
    }
    override fun bindView(file: File) {
        GlideApp.with(context)
            .load(file)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(ivStatus)
    }

    override fun setOnClickListener(listener: View.OnClickListener) = view.setOnClickListener(listener)

    override fun statusType(): Int = FileType.TYPE_VIDEO
}