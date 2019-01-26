package com.softinit.whatsdirect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.softinit.whatsdirect.utils.FileType
import java.io.File

class PreviewViewPagerAdapter: PagerAdapter {

    private val context: Context
    private val initialFile: File
    private var mediaDirectory: List<File>
    private var isDirectoryInitialized: Boolean = false
    constructor(_context: Context, file: File): super() {
        context = _context
        initialFile = file
        mediaDirectory = initialFile.parentFile.listFiles().filter { FileType.isFileImageVideo(it) }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(context).inflate(0, container, false)
        
        return super.instantiateItem(container, position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int = 0
    override fun isViewFromObject(view: View, `object`: Any): Boolean = (view == `object`)
}