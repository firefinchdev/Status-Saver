package com.softinit.whatsdirect.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.adapters.MainViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.softinit.whatsdirect.utils.hasPermissions

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    companion object {
        const val KEY_REQUEST_PERMISSION = 1
        const val KEY_REQUEST_CALL_LOG_PERMISSION = 2
        const val KEY_REQUEST_WRITE_EXTERNAL_PERMISSION = 3
        private var askedForPermissionsOnce = false
    }
    private lateinit var mMainViewPagerAdapter: MainViewPagerAdapter

    private lateinit var mViewPager: androidx.viewpager.widget.ViewPager

    private lateinit var mTabLayout: TabLayout
    private lateinit var appBarLayout: AppBarLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupIds()

        val PERMISSIONS = arrayOf(Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        setup()
        if (!askedForPermissionsOnce && !hasPermissions(this, PERMISSIONS)) {
            askedForPermissionsOnce = true
            ActivityCompat.requestPermissions(this, PERMISSIONS, KEY_REQUEST_PERMISSION)
        }

    }
    private fun setupIds() {
        mViewPager = findViewById(R.id.mainViewPager)
        mTabLayout = findViewById(R.id.mainTabLayout)
        appBarLayout = findViewById(R.id.appBarMain)
    }

    private fun setup() {
        setupViewPager()
    }

    private fun setupViewPager() {
        mMainViewPagerAdapter = MainViewPagerAdapter(supportFragmentManager)

        mViewPager.adapter = mMainViewPagerAdapter
        mTabLayout.setupWithViewPager(mViewPager)
        mViewPager.addOnPageChangeListener(this)
    }

    //TODO: Dont allow SEND btn if phone number is black
    //TODO: Remove keyboard on fragment scroll
    //TODO: Last selected country
    override fun onBackPressed() {
        if (mViewPager.currentItem != 0) {
            mViewPager.setCurrentItem(0, true)
        } else {
            super.onBackPressed()
        }
    }

    override fun onPageSelected(position: Int) {
        when(position) {
            MainViewPagerAdapter.POS_MESSAGE -> appBarLayout.setExpanded(true, true)
            MainViewPagerAdapter.POS_SAVED_STATUS -> (mViewPager.adapter as MainViewPagerAdapter)
                .refreshSavedStatus()
        }

    }
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    private fun restart() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(0, 0)
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            KEY_REQUEST_PERMISSION -> {
                if (grantResults.isNotEmpty() &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    restart()
                }
            }
            KEY_REQUEST_CALL_LOG_PERMISSION -> {
                if (grantResults.isNotEmpty() &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    restart()
                }
            }
            KEY_REQUEST_WRITE_EXTERNAL_PERMISSION -> {
                if (grantResults.isNotEmpty() &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    restart()
                }
            }
        }
    }
}
