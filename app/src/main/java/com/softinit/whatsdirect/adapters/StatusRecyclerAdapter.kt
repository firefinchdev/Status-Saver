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
    companion object {
        val WHATSAPP_DIR: File = File(Environment.getExternalStorageDirectory(), "WhatsApp/Media/.Statuses")    //TODO: Fix FC when this directory is not available
//        val WHATSAPP_DIR: File = File(Environment.getExternalStorageDirectory(), "WhatsApp/Media/.status_copy")
    }

    private var activity: Activity
    private var fileList: MutableList<File>
    private var fileObserver: FileObserver

    constructor(activity: Activity, fileDirectory: File, addFileAction: (()-> Unit)? = null):super() {
        if (!fileDirectory.isDirectory) throw Error("Fuck You! I expected a directory.")
        this.activity = activity
        this.fileList = fileDirectory.listFiles().filter { isFileImageVideo(it) }.toMutableList()
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
            i.putExtra(PreviewActivity.INTENT_EXTRA_FILE_ABSOLUTE_PATH, file.absolutePath)
            activity.startActivity(i)
        })
    }

    override fun getItemCount(): Int = fileList.size

    override fun getItemViewType(position: Int): Int {
        return getFileType(fileList[position])
    }

    abstract class StatusViewHolder: RecyclerView.ViewHolder {
        constructor(view: View) : super(view)
        abstract fun bindView(file: File)
        abstract fun statusType(): Int
        abstract fun setOnClickListener(listener: View.OnClickListener)
    }
}

