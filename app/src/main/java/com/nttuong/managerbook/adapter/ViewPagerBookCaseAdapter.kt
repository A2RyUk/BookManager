package com.nttuong.managerbook.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nttuong.managerbook.fragment.bookcase.FavoriteBookFragment
import com.nttuong.managerbook.fragment.bookcase.ReadRecentlyFragment
import com.nttuong.managerbook.fragment.bookcase.WasDownloadFragment

class ViewPagerBookCaseAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    var fragments : ArrayList<Fragment> = ArrayList()

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyItemInserted(fragments.size-1)
    }
}