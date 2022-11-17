package com.nttuong.managerbook.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nttuong.managerbook.fragment.bookcase.FavoriteBookFragment
import com.nttuong.managerbook.fragment.bookcase.ReadRecentlyFragment
import com.nttuong.managerbook.fragment.bookcase.WasDownloadFragment

class ViewPagerBookCaseAdapter(
    fragment: Fragment
) :FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoriteBookFragment()
            1 -> ReadRecentlyFragment()
            2 -> WasDownloadFragment()
            else -> throw IllegalArgumentException("Unknown fragment for position $position")
        }
    }
}