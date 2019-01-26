package com.softinit.whatsdirect.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.FileObserver
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.softinit.whatsdirect.activities.PreviewActivity
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
//        val WHATSAPP_DIR: File = File(Environment.getExternalStorageDirectory(), "WhatsApp/Media/.Statuses")
        val WHATSAPP_DIR: File = File(Environment.getExternalStorageDirectory(), "WhatsApp/Media/.status_copy")
    }

    private var activity: Activity
    private var fileList: MutableList<File>
    private var fileObserver: FileObserver

    constructor(activity: Activity, fileDirectory: File, addFileAction: (()-> Unit)? = null):super() {
        if (!fileDirectory.isDirectory) throw Error("Fuck You! I expected a directory.")
        this.activity = activity
        this.fileList = fileDirectory.listFiles().filter { f -> getFileType(f).let { it == TYPE_IMAGE || it == TYPE_VIDEO } }.toMutableList()
        fileObserver = object: FileObserver(fileDirectory.absolutePath, FileObserver.CREATE) {
            override fun onEvent(event: Int, path: String?) {
                when (event) {
                    FileObserver.CREATE -> {
                        // Path is just the name of the file
                        val f: File = File(fileDirectory, path)
                        if (getFileType(f).let { it == TYPE_IMAGE || it == TYPE_VIDEO }) {
                            fileList.add(0,f)
                            activity.runOnUiThread {
                                notifyItemInserted(0)
                                addFileAction?.invoke()   //Call addFileAction only if its not null
                            }
                        }
                    }
                }
            }
        }.apply { startWatching() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val viewHolder: StatusViewHolder = when (viewType) {
            TYPE_IMAGE -> ImageViewHolder.inflateView(activity, parent)
            TYPE_VIDEO -> VideoViewHolder.inflateView(activity, parent)
            else -> throw Error("Unsupported File Found")
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val file: File = fileList[position]
        holder.bindView(file)
        holder.setOnClickListener(View.OnClickListener {
            val i: Intent = Intent(activity, PreviewActivity::class.java)
            i.putExtra(PreviewActivity.INTENT_EXTRA_PATH, file.absolutePath)
            activity.startActivity(i)
        })
    }

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
        abstract fun setOnClickListener(listener: View.OnClickListener)
    }
}

