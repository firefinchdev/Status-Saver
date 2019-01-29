package com.softinit.whatsdirect.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.softinit.whatsdirect.fragments.MessageFragment
import com.softinit.whatsdirect.fragments.SavedStatusFragment
import com.softinit.whatsdirect.fragments.StatusFragment

class MainViewPagerAdapter(fm: androidx.fragment.app.FragmentManager): androidx.fragment.app.FragmentPagerAdapter(fm) {

    companion object {
        const val POS_MESSAGE = 0
        const val POS_STATUS = 1
        const val POS_SAVED_STATUS = 2
    }
    private val pages: Array<Fragment> = arrayOf(MessageFragment(), StatusFragment(), SavedStatusFragment())

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return when(position) {
            in 0..pages.size -> pages[position]
            else -> androidx.fragment.app.Fragment()
        }
    }

    override fun getCount(): Int = pages.size

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            POS_MESSAGE -> "Message"
            POS_STATUS -> "Status"
            POS_SAVED_STATUS -> "Saved"
            else -> "Unexpected"
        }
    }

    fun refreshSavedStatus() = (pages[POS_SAVED_STATUS] as SavedStatusFragment).refresh()
}