package com.softinit.whatsdirect.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.adapters.MainViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var mMainViewPagerAdapter: MainViewPagerAdapter
    private lateinit var mViewPager: androidx.viewpager.widget.ViewPager
    private lateinit var mTablayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()

    }

    fun setup() {
        setupViewPager()
    }

    fun setupViewPager() {
        mMainViewPagerAdapter = MainViewPagerAdapter(supportFragmentManager)
        mViewPager = findViewById(R.id.mainViewPager)
        mTablayout = findViewById(R.id.mainTabLayout)

        mViewPager.adapter = mMainViewPagerAdapter
        mTablayout.setupWithViewPager(mViewPager)
    }
}
