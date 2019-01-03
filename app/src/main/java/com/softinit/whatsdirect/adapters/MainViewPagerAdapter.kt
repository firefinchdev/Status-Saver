package com.softinit.whatsdirect.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.softinit.whatsdirect.fragments.MessageFragment
import com.softinit.whatsdirect.fragments.StatusFragment

class MainViewPagerAdapter(fm: androidx.fragment.app.FragmentManager): androidx.fragment.app.FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return when(position) {
            0 -> MessageFragment()
            1 -> StatusFragment()
            else -> androidx.fragment.app.Fragment()
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