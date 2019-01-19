package com.softinit.whatsdirect.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.adapters.MainViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var mMainViewPagerAdapter: MainViewPagerAdapter
    private lateinit var mViewPager: androidx.viewpager.widget.ViewPager
    private lateinit var mTabLayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        setup()
    }

    private fun setup() {
        setupViewPager()
    }

    private fun setupViewPager() {
        mMainViewPagerAdapter = MainViewPagerAdapter(supportFragmentManager)
        mViewPager = findViewById(R.id.mainViewPager)
        mTabLayout = findViewById(R.id.mainTabLayout)

        mViewPager.adapter = mMainViewPagerAdapter
        mTabLayout.setupWithViewPager(mViewPager)
    }
}
