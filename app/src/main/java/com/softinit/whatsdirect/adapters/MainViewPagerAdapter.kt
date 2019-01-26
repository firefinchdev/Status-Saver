package com.softinit.whatsdirect.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.softinit.whatsdirect.fragments.MessageFragment
import com.softinit.whatsdirect.fragments.StatusFragment

class MainViewPagerAdapter(fm: androidx.fragment.app.FragmentManager): androidx.fragment.app.FragmentPagerAdapter(fm) {

    private val pages: Array<Fragment> = arrayOf(MessageFragment(), StatusFragment())

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return when(position) {
            in 0..pages.size -> pages[position]
            else -> androidx.fragment.app.Fragment()
        }
    }

    override fun getCount(): Int = pages.size

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Message"
            1 -> "Status"
            else -> "Unexpected"
        }
    }
}