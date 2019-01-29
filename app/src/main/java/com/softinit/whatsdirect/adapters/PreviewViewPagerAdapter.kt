package com.softinit.whatsdirect.adapters

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
import com.bumptech.glide.Glide
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.utils.FileType
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
        Glide.with(context)
            .load(mediaDirectory[position])
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
                val uri = FileProvider.getUriForFile(context, "com.softinit.whatsdirect", file)
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
        if (!isDirectoryInitialized) {
            isDirectoryInitialized = true
            var newMediaList: MutableList<File> = mutableListOf()
            withContext(Dispatchers.Default) {
                newMediaList = initialFile.parentFile.listFiles().filter{ FileType.isFileImageVideo(it) }.toMutableList()
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