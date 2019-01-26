package com.softinit.whatsdirect.viewholders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.adapters.StatusRecyclerAdapter
import com.softinit.whatsdirect.utils.FileType
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
        Glide.with(context)
            .load(file)
            .into(ivStatus)
    }

    override fun setOnClickListener(listener: View.OnClickListener) = view.setOnClickListener(listener)

    override fun statusType(): Int = FileType.TYPE_IMAGE
}