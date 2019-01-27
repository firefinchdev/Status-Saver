package com.softinit.whatsdirect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var mediaDirectory: List<File>
    private var isDirectoryInitialized: Boolean = false
    private var viewPager: ViewPager
    constructor(_context: Context, file: File, pager: ViewPager): super() {
        context = _context
        initialFile = file
        mediaDirectory = listOf(initialFile)
        viewPager = pager
//        mediaDirectory = initialFile.parentFile.listFiles().filter { FileType.isFileImageVideo(it) }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_image_status_preview, container, false)
        Glide.with(context)
            .load(mediaDirectory[position])
            .into(view.findViewById(R.id.meow))
        container.addView(view as ViewGroup)
        initializeDirectory()
        return view
    }

    private inline fun initializeDirectory()= GlobalScope.launch {
        if (!isDirectoryInitialized) {
            var newMediaList: List<File> = listOf()
            withContext(Dispatchers.Default) {
                newMediaList = initialFile.parentFile.listFiles().filter { FileType.isFileImageVideo(it) }
                isDirectoryInitialized = true
            }
            withContext(Dispatchers.Main) {
                mediaDirectory = newMediaList
                notifyDataSetChanged()
                val currentItem = {
                    var idx = 0
                    mediaDirectory.forEachIndexed {i,ele ->
                        if(ele.name == initialFile.name)idx=i
                    }
                    idx
                }()
                viewPager.setCurrentItem( currentItem,false)
            }
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int = mediaDirectory.size
    override fun isViewFromObject(view: View, `object`: Any): Boolean = (view == `object`)
}