package com.firefinch.wastatus.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firefinch.wastatus.BuildConfig
import com.firefinch.wastatus.GlideApp
import com.firefinch.wastatus.R
import com.firefinch.wastatus.utils.FileType
import com.firefinch.wastatus.utils.listMediaFiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PreviewViewPagerAdapter: PagerAdapter {

    private val context: Context
    private val initialFile: File
    private var mediaDirectory: MutableList<File>
    private var isDirectoryInitialized: Boolean = false
    private var viewPager: ViewPager
    constructor(_context: Context, file: File, pager: ViewPager): super() {
        context = _context
        initialFile = file
        mediaDirectory = mutableListOf(initialFile)
        viewPager = pager
//        mediaDirectory = initialFile.parentFile.listFiles().filter { FileType.isFileImageVideo(it) }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_image_status_preview, container, false)
        GlideApp.with(context)
            .load(mediaDirectory[position])
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(view.findViewById(R.id.image_view_media))
        setupPlay(view, position)
        container.addView(view as ViewGroup)
        initializeDirectory()
        return view
    }

    private fun setupPlay(view: View, position: Int) {
        val file = mediaDirectory[position]
        val ivPlayVideo: ImageView = view.findViewById(R.id.iv_play_video)
        if (FileType.isFileVideo(mediaDirectory[position])) {
            ivPlayVideo.setOnClickListener {
                val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file)
                context.startActivity(Intent(Intent.ACTION_VIEW, uri).apply {
                    setDataAndType(uri, "video/mp4")
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                })
            }
        } else {
            ivPlayVideo.visibility = View.GONE
        }
    }

    private inline fun initializeDirectory()= GlobalScope.launch {
        // This "init" and synchronized is so to ensure
        // multiple threads don't initiate more than once
        // (or so that main thread does not initialize
        // directory twice after dispatching new thread.
        var init = true
        synchronized("LOCK") {
            init = isDirectoryInitialized
            if (!init) {
                isDirectoryInitialized = true
            }
        }
        if (!init) {
            var newMediaList: MutableList<File> = mutableListOf()
            withContext(Dispatchers.Default) {
                newMediaList = listMediaFiles(initialFile.parentFile)
            }
            withContext(Dispatchers.Main) {
                mediaDirectory.clear()
                mediaDirectory.addAll(newMediaList)
                (context as Activity).runOnUiThread { notifyDataSetChanged() }
                val currentItem = {
                    var idx = 0
                    mediaDirectory.forEachIndexed {i,ele ->
                        if(ele.name == initialFile.name)idx = i
                    }
                    idx
                }()
                viewPager.setCurrentItem( currentItem,false)
            }
        }
    }

    //This is to resolve the bug where 1st item is rendered same as 2nd item
    //Happens when touching 2nd item followed by left swipe.
    //This method will help destroy all the views on call to notifyDataSetChanged()
    override fun getItemPosition(`object`: Any): Int = POSITION_NONE

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun getCurrentMediaFile() = mediaDirectory[viewPager.currentItem]

    override fun getCount(): Int = mediaDirectory.size
    override fun isViewFromObject(view: View, `object`: Any): Boolean = (view == `object`)
}