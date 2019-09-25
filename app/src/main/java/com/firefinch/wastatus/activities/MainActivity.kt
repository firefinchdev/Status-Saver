package com.firefinch.wastatus.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.firefinch.wastatus.R
import com.firefinch.wastatus.adapters.MainViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import com.firefinch.wastatus.utils.*

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    companion object {
        const val KEY_REQUEST_PERMISSION = 1
        const val KEY_REQUEST_CALL_LOG_PERMISSION = 2
        const val KEY_REQUEST_WRITE_EXTERNAL_PERMISSION = 3
        private var askedForPermissionsOnce = false
    }
    private lateinit var mMainViewPagerAdapter: MainViewPagerAdapter
    private lateinit var mViewPager:ViewPager

    private lateinit var mTabLayout: TabLayout
    private lateinit var appBarLayout: AppBarLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupIds()

        val PERMISSIONS = arrayOf(
//            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        setup()
        if (!askedForPermissionsOnce && !hasPermissions(this, PERMISSIONS)) {
            askedForPermissionsOnce = true
            ActivityCompat.requestPermissions(this, PERMISSIONS, KEY_REQUEST_PERMISSION)
        }

        handleIntent()
        //Intentionally crash app
        //throw RuntimeException("Boom!")
    }

    private fun handleIntent() {
        when(intent?.action) {
            Intent.ACTION_VIEW, Intent.ACTION_DIAL -> {
                mMainViewPagerAdapter.setDirectPhoneNumber(intent.data?.toString()?.substring(4) ?: "")
            }
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
        mMainViewPagerAdapter = MainViewPagerAdapter(this, supportFragmentManager)

        mViewPager.adapter = mMainViewPagerAdapter
        mTabLayout.setupWithViewPager(mViewPager)
        mViewPager.addOnPageChangeListener(this)
    }

    //TODO: Dont allow SEND btn if phone number is black
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

        // Hide Soft Keyboard on Page Change
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(mViewPager.windowToken, 0)

    }
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    private fun restart() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            // Apply any intents passed to activity before restart
            // Phone Number intent-filter for DIAL - Start
            action = intent.action
            data = intent.data
            // Phone Number intent-filter for DIAL - End
        })
        overridePendingTransition(0, 0)
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            KEY_REQUEST_PERMISSION -> {
                if (grantResults.isNotEmpty() &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_share -> startActivity(AppIntent.share(this@MainActivity))
            R.id.action_rate -> AppIntent.startPlayStore(this@MainActivity)
            R.id.action_improve -> startActivity(AppIntent.devMail(this@MainActivity))
            R.id.action_about -> startActivity(Intent(this@MainActivity, AboutActivity::class.java))
        }
        return true
    }
}
