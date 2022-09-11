package com.nttuong.managerbook.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nttuong.managerbook.fragment.author.AuthorFragment
import com.nttuong.managerbook.fragment.book.BookFragment
import com.nttuong.managerbook.fragment.category.CategoryFragment
import java.lang.IllegalArgumentException

class ViewPagerAdapter(
    activity: AppCompatActivity
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> BookFragment()
            1 -> CategoryFragment()
            2 -> AuthorFragment()
            else -> throw IllegalArgumentException("Unknown fragment for position $position")
        }
    }
}