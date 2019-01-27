package com.softinit.whatsdirect.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.adapters.PreviewViewPagerAdapter
import com.softinit.whatsdirect.extended.ImageViewTouchViewPager
import java.io.File

class PreviewActivity : AppCompatActivity() {

    private lateinit var previewViewPager: ImageViewTouchViewPager
    private lateinit var mediaFile: File

    companion object {
        const val INTENT_EXTRA_FILE_ABSOLUTE_PATH = "INTENT_EXTRA_FILE_ABSOLUTE_PATH"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        setupIds()

        intent.extras?.apply { mediaFile = File(getString(INTENT_EXTRA_FILE_ABSOLUTE_PATH)) }

        setupMediaViewPager(mediaFile)
    }

    private fun setupIds() {
        previewViewPager = findViewById(R.id.view_pager_media)
    }

    private fun setupMediaViewPager(file: File) {
        previewViewPager.adapter = PreviewViewPagerAdapter(this, file, previewViewPager)
    }
}
