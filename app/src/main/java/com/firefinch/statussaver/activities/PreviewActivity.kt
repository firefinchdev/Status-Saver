package com.firefinch.statussaver.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.firefinch.statussaver.BuildConfig
import com.firefinch.statussaver.GlideApp
import com.firefinch.statussaver.R
import com.firefinch.statussaver.adapters.PreviewViewPagerAdapter
import com.firefinch.statussaver.extended.ImageViewTouchViewPager
import com.firefinch.statussaver.utils.DIR_SAVED_STATUS
import com.firefinch.statussaver.utils.FileType
import com.firefinch.statussaver.utils.getDeleteStatusDialog
import com.firefinch.statussaver.utils.getRenameStatusDialog
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
    private lateinit var ivHome: ImageView
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
        ivHome = findViewById(R.id.iv_home)
    }

    private fun setup() {
        listOf(fabSave, fabShare, fabDelete, ivHome).forEach {
            it.setOnClickListener(this)
        }
        setupMediaViewPager(mediaFile)
    }

    private fun setupMediaViewPager(file: File) {
        previewViewPager.adapter = PreviewViewPagerAdapter(this, file, previewViewPager)
    }

    override fun onClick(v: View?) {
        val srcFile = (previewViewPager.adapter as PreviewViewPagerAdapter).getCurrentMediaFile()
        when(v?.id) {
            R.id.fab_save -> {
                try {
                    FileUtils.copyFileToDirectory(srcFile, DIR_SAVED_STATUS, false)
                    getSaveSuccessSnackBar(File(DIR_SAVED_STATUS, srcFile.name)).show()

                } catch (e: IOException) {
                    getSaveFailSnackBar().show()
                }
            }
            R.id.fab_share -> {
                val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, srcFile)
                val i = Intent(Intent.ACTION_SEND).apply {
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    when (FileType.getFileType(srcFile)){
                        FileType.TYPE_IMAGE -> setType("image/*")
                        FileType.TYPE_VIDEO -> setType("video/*")
                    }
                    putExtra(Intent.EXTRA_STREAM, uri)
                }
                startActivity(Intent.createChooser(i, "Share with"))
            }
            R.id.fab_delete -> {
                getDeleteStatusDialog(this@PreviewActivity) {
                    if (!srcFile.isDirectory) {
                        srcFile.delete()
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }.show()
            }
            R.id.iv_home -> finish()
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
