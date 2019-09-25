package com.firefinch.wastatus.adapters

import android.app.Activity
import android.content.Intent
import android.os.FileObserver
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.firefinch.wastatus.activities.PreviewActivity
import com.firefinch.wastatus.utils.FileType.TYPE_IMAGE
import com.firefinch.wastatus.utils.FileType.TYPE_VIDEO
import com.firefinch.wastatus.utils.FileType.getFileType
import com.firefinch.wastatus.utils.hasPermissions
import com.firefinch.wastatus.utils.listMediaFiles
import com.firefinch.wastatus.viewholders.ImageViewHolder
import com.firefinch.wastatus.viewholders.VideoViewHolder
import java.io.File
import java.lang.Error

class StatusRecyclerAdapter: Adapter<StatusRecyclerAdapter.StatusViewHolder> {
    private var activity: Activity
    private lateinit var fileDirectory: File
    private var fileList: MutableList<File>
    private lateinit var fileObserver: FileObserver
    private var mOptions: AdapterOptions
    private var onStatusAdapterActions: OnStatusAdapterActions?
    private var hasReadExtStoragePermission: Boolean

    constructor(activity: Activity, fileDirectory: File, onStatusAdapPerm: OnStatusAdapterActions? = null, _options: AdapterOptions = options()): super() {
        this.activity = activity
        this.mOptions = _options
        onStatusAdapterActions = onStatusAdapPerm

        if (!hasPermissions(activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            hasReadExtStoragePermission = false
            this.fileList = mutableListOf()
            onStatusAdapterActions?.adapterNotHasPermissions()
            return
        } else {
            hasReadExtStoragePermission = true
            onStatusAdapterActions?.adapterHasPermissions()
        }

        if (!fileDirectory.exists()) {
            fileDirectory.mkdirs()
        }
        if (!fileDirectory.isDirectory) throw Error("Bro, I expected a directory.")
        this.fileDirectory = fileDirectory


        this.fileList = listMediaFiles(fileDirectory)
        if (this.fileList.size == 0) {
            onStatusAdapterActions?.adapterEmptyStatusDirectory()
        }
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
            i.putExtra(PreviewActivity.ALLOW_SAVE, mOptions.allowSave)
            i.putExtra(PreviewActivity.ALLOW_DELETE, mOptions.allowDelete)
            i.putExtra(PreviewActivity.ALLOW_SHARE, mOptions.allowShare)
            activity.startActivity(i)
        })
    }

    override fun getItemCount(): Int = fileList.size

    override fun getItemViewType(position: Int): Int {
        return getFileType(fileList[position])
    }

    fun refresh() {
        if (hasReadExtStoragePermission) {
            onStatusAdapterActions?.adapterHasPermissions()
            this.fileList = listMediaFiles(fileDirectory)
            if (this.fileList.size == 0) {
                onStatusAdapterActions?.adapterEmptyStatusDirectory()
            }
            notifyDataSetChanged()
        } else {
            onStatusAdapterActions?.adapterNotHasPermissions()
        }
    }

    abstract class StatusViewHolder: RecyclerView.ViewHolder {
        constructor(view: View) : super(view)
        abstract fun bindView(file: File)
        abstract fun statusType(): Int
        abstract fun setOnClickListener(listener: View.OnClickListener)
    }

    companion object {
        @JvmStatic
        // Note that this has to be a function, because if its a variable, its value will be changed
        // everytime someone calls to it
        fun options() = object: AdapterOptions {
            override var allowSave: Boolean = false
            override var allowShare: Boolean = false
            override var allowDelete: Boolean = false
        }
    }

    interface AdapterOptions {
        var allowSave: Boolean
        var allowShare: Boolean
        var allowDelete: Boolean
    }

    interface OnStatusAdapterActions {
        fun adapterHasPermissions()
        fun adapterNotHasPermissions()
        fun adapterEmptyStatusDirectory()
    }
}

