package com.softinit.whatsdirect.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.adapters.MainViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {
    private lateinit var mMainViewPagerAdapter: MainViewPagerAdapter

    private lateinit var mViewPager: androidx.viewpager.widget.ViewPager

    private lateinit var mTabLayout: TabLayout
    private lateinit var appBarLayout: AppBarLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupIds()
        setup()
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
        appBarLayout.setExpanded(true, true)
    }
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
}
