package com.softinit.whatsdirect.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.softinit.whatsdirect.fragments.MessageFragment
import com.softinit.whatsdirect.fragments.StatusFragment

class MainViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> MessageFragment()
            1 -> StatusFragment()
            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Message"
            1 -> "Status"
            else -> "Unexpected"
        }
    }
}