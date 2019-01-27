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
import com.softinit.whatsdirect.utils.FileType.TYPE_IMAGE
import com.softinit.whatsdirect.utils.FileType.TYPE_VIDEO
import com.softinit.whatsdirect.utils.FileType.getFileType
import com.softinit.whatsdirect.utils.FileType.isFileImageVideo
import com.softinit.whatsdirect.viewholders.ImageViewHolder
import com.softinit.whatsdirect.viewholders.VideoViewHolder
import java.io.File
import java.lang.Error
import java.net.URLConnection

class StatusRecyclerAdapter: Adapter<StatusRecyclerAdapter.StatusViewHolder> {
    private var activity: Activity
    private var fileDirectory: File
    private var fileList: MutableList<File>
    private var fileObserver: FileObserver

    constructor(activity: Activity, fileDirectory: File): super() {
        if (!fileDirectory.isDirectory) throw Error("Bro, I expected a directory.")
        this.activity = activity
        this.fileDirectory = fileDirectory
        this.fileList = fileDirectory.listFiles().filter { isFileImageVideo(it) }.toMutableList()
        fileObserver = object: FileObserver(fileDirectory.absolutePath, FileObserver.DELETE) {
            override fun onEvent(event: Int, path: String?) {
                when (event) {
                    FileObserver.DELETE -> {
                        // Path is just the name of the file
                        val f: File = File(fileDirectory, path)
                        if (getFileType(f).let { it == TYPE_IMAGE || it == TYPE_VIDEO }) {
                            fileList.remove(f)
                            activity.runOnUiThread {
                                notifyDataSetChanged()
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
            i.putExtra(PreviewActivity.INTENT_EXTRA_FILE_ABSOLUTE_PATH, file.absolutePath)
            activity.startActivity(i)
        })
    }

    override fun getItemCount(): Int = fileList.size

    override fun getItemViewType(position: Int): Int {
        return getFileType(fileList[position])
    }

    fun refresh() {
        this.fileList = fileDirectory.listFiles().filter { isFileImageVideo(it) }.toMutableList()
        notifyDataSetChanged()
    }
    abstract class StatusViewHolder: RecyclerView.ViewHolder {
        constructor(view: View) : super(view)
        abstract fun bindView(file: File)
        abstract fun statusType(): Int
        abstract fun setOnClickListener(listener: View.OnClickListener)
    }
}

