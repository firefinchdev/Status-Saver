package com.softinit.whatsdirect.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.adapters.PreviewViewPagerAdapter
import com.softinit.whatsdirect.extended.ImageViewTouchViewPager
import com.softinit.whatsdirect.utils.DIR_SAVED_STATUS
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException

class PreviewActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var previewViewPager: ImageViewTouchViewPager

    private lateinit var mediaFile: File
    private lateinit var fabSave: FloatingActionButton
    companion object {

        const val INTENT_EXTRA_FILE_ABSOLUTE_PATH = "INTENT_EXTRA_FILE_ABSOLUTE_PATH"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        setupIds()

        intent.extras?.apply { mediaFile = File(getString(INTENT_EXTRA_FILE_ABSOLUTE_PATH)) }

        setup()
    }

    private fun setupIds() {
        previewViewPager = findViewById(R.id.view_pager_media)
        fabSave = findViewById(R.id.fab_save)
    }

    private fun setup() {
        fabSave.setOnClickListener(this)
        setupMediaViewPager(mediaFile)
    }

    private fun setupMediaViewPager(file: File) {
        previewViewPager.adapter = PreviewViewPagerAdapter(this, file, previewViewPager)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.fab_save -> {
                val srcFile = (previewViewPager.adapter as PreviewViewPagerAdapter).getCurrentMediaFile()
                try {
                    FileUtils.copyFileToDirectory(srcFile, DIR_SAVED_STATUS)
                    Toast.makeText(this, "Status Saved", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    Log.d("FUCK", e.message)
                    Toast.makeText(this, "Failed to Save Status", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
