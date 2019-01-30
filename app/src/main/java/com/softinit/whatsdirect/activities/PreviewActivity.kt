package com.softinit.whatsdirect.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.softinit.whatsdirect.GlideApp
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.adapters.PreviewViewPagerAdapter
import com.softinit.whatsdirect.extended.ImageViewTouchViewPager
import com.softinit.whatsdirect.utils.DIR_SAVED_STATUS
import com.softinit.whatsdirect.utils.getRenameStatusDialog
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException

class PreviewActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val INTENT_EXTRA_FILE_ABSOLUTE_PATH = "INTENT_EXTRA_FILE_ABSOLUTE_PATH"
        const val ALLOW_SAVE = "ALLOW_SAVE"
        const val ALLOW_SHARE = "ALLOW_SHARE"
        const val ALLOW_DELETE = "ALLOW_DELETE"
    }

    private lateinit var previewViewPager: ImageViewTouchViewPager
    private lateinit var mediaFile: File
    private lateinit var fabSave: FloatingActionButton
    private lateinit var fabShare: FloatingActionButton
    private lateinit var fabDelete: FloatingActionButton
    private lateinit var coordinatorLayout: CoordinatorLayout
    private var allowSave: Boolean = false
    private var allowShare: Boolean = false
    private var allowDelete: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        setupIds()

        intent.extras?.apply {
            mediaFile = File(getString(INTENT_EXTRA_FILE_ABSOLUTE_PATH))
            allowSave = getBoolean(ALLOW_SAVE, false)
            allowShare = getBoolean(ALLOW_SHARE, false)
            allowDelete = getBoolean(ALLOW_DELETE, false)
        }

        setup()
        setupVisibilities()
    }

    private fun setupVisibilities() {
        if (!allowSave) {
            fabSave.visibility = View.GONE
        }
        if (!allowShare) {
            fabShare.visibility = View.GONE
        }
        if (!allowDelete) {
            fabDelete.visibility = View.GONE
        }
    }

    private fun setupIds() {
        previewViewPager = findViewById(R.id.view_pager_media)
        fabSave = findViewById(R.id.fab_save)
        fabShare = findViewById(R.id.fab_share)
        fabDelete = findViewById(R.id.fab_delete)
        coordinatorLayout = findViewById(R.id.preview_coordinator_layout)
    }

    private fun setup() {
        listOf(fabSave, fabShare, fabDelete).forEach {
            it.setOnClickListener(this)
        }
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
                    FileUtils.copyFileToDirectory(srcFile, DIR_SAVED_STATUS, false)
                    getSaveSuccessSnackBar(File(DIR_SAVED_STATUS, srcFile.name)).show()

                } catch (e: IOException) {
                    getSaveFailSnackBar().show()
                }
            }
            R.id.fab_share -> {
                //TODO
            }
            R.id.fab_delete -> {
                //TODO
            }
        }
    }

    private fun getSaveSuccessSnackBar(file: File) =
        Snackbar.make(coordinatorLayout,"Status Saved!",Snackbar.LENGTH_LONG)
            .apply {
                setAction("Edit Name") {
                    getRenameStatusDialog(this@PreviewActivity, file).show()
                }
            }

    override fun onDestroy() {
        super.onDestroy()
        GlideApp.get(this).clearMemory();
    }

    private fun getSaveFailSnackBar() =
        Snackbar.make(coordinatorLayout, "Failed to Save Status", Snackbar.LENGTH_LONG)
}
