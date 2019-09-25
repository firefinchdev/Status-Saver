package com.firefinch.wastatus.viewholders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firefinch.wastatus.GlideApp
import com.firefinch.wastatus.R
import com.firefinch.wastatus.adapters.StatusRecyclerAdapter
import com.firefinch.wastatus.utils.FileType
import java.io.File

class ImageViewHolder: StatusRecyclerAdapter.StatusViewHolder {

    companion object {
        @JvmStatic
        fun inflateView(context: Context, parent: ViewGroup): ImageViewHolder = ImageViewHolder(context, LayoutInflater.from(context)
                .inflate(R.layout.list_item_image_status, parent, false))
    }

    val context: Context
    val view: View
    val ivStatus: ImageView

    constructor(context: Context, _view: View): super(_view) {
        this.context = context
        this.view = _view
        ivStatus = view.findViewById(R.id.iv_status_image)
    }
    override fun bindView(file: File) {
        GlideApp.with(context)
            .load(file)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(ivStatus)
    }

    override fun setOnClickListener(listener: View.OnClickListener) = view.setOnClickListener(listener)

    override fun statusType(): Int = FileType.TYPE_IMAGE
}