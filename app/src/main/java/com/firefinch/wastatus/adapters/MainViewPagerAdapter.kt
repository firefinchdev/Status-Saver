package com.firefinch.wastatus.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import com.firefinch.wastatus.fragments.MessageFragment
import com.firefinch.wastatus.fragments.SavedStatusFragment
import com.firefinch.wastatus.fragments.StatusFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MainViewPagerAdapter(
    private val context: Context,
    fm: FragmentManager): FragmentPagerAdapter(fm) {

    companion object {
        const val POS_MESSAGE = 0
        const val POS_STATUS = 1
        const val POS_SAVED_STATUS = 2
    }
    private val MESSAGE_FRAGMENT = MessageFragment()
    private val STATUS_FRAGMENT = StatusFragment()
    private val SAVED_STATUS_FRAGMENT = SavedStatusFragment()

    private val pages: Array<Fragment> = arrayOf(MESSAGE_FRAGMENT, STATUS_FRAGMENT, SAVED_STATUS_FRAGMENT)

    override fun getItem(position: Int): Fragment {
        return when(position) {
            in 0..pages.size -> pages[position]
            else -> Fragment()
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

    fun refreshSavedStatus() = SAVED_STATUS_FRAGMENT.refresh()

    fun setDirectPhoneNumber(phoneNum: String) = MESSAGE_FRAGMENT.scheduleDirectPhoneNumber(phoneNum)
}