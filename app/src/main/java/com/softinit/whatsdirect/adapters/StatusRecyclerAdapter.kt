package com.softinit.whatsdirect.adapters

import android.content.Context
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.softinit.whatsdirect.viewholders.ImageViewHolder
import com.softinit.whatsdirect.viewholders.VideoViewHolder
import java.io.File
import java.lang.Error
import java.net.URLConnection

class StatusRecyclerAdapter: Adapter<StatusRecyclerAdapter.StatusViewHolder> {
    companion object {
        const val TYPE_DIRECTORY = 1
        const val TYPE_IMAGE = 2
        const val TYPE_VIDEO = 3
        const val TYPE_OTHER = 4
        val WHATSAPP_DIR: File = File(Environment.getExternalStorageDirectory(), "WhatsApp/Media/.Statuses")
    }

    private var context: Context
    private var fileList: List<File>

    constructor(context: Context, fileDirectory: File):super() {
        this.context = context
        this.fileList = fileDirectory.listFiles().filter { f -> getFileType(f).let { it == TYPE_IMAGE || it == TYPE_VIDEO } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder =
        when(viewType) {
            TYPE_IMAGE -> ImageViewHolder.inflateView(context, parent)
            TYPE_VIDEO -> VideoViewHolder.inflateView(context, parent)
            else -> throw Error("Unsupported File Found")
        }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) =
        holder.bindView(fileList[position])

    override fun getItemCount(): Int = fileList.size

    override fun getItemViewType(position: Int): Int {
        return getFileType(fileList[position])
    }

    private fun getFileType(file: File): Int {
        if (file.isDirectory) return TYPE_DIRECTORY
        URLConnection.guessContentTypeFromName(file.name).let {
            if (it.startsWith("image")) return TYPE_IMAGE
            if (it.startsWith("video")) return TYPE_VIDEO
        }
        return TYPE_OTHER
    }

    abstract class StatusViewHolder: RecyclerView.ViewHolder {
        constructor(view: View) : super(view)
        abstract fun bindView(file: File)
        abstract fun statusType(): Int
    }
}

