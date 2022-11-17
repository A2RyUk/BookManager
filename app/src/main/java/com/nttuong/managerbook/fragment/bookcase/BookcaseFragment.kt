package com.nttuong.managerbook.fragment.bookcase

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nttuong.managerbook.R
import com.nttuong.managerbook.adapter.ViewPagerBookCaseAdapter
import com.nttuong.managerbook.databinding.FragmentBookcaseBinding

class BookcaseFragment : Fragment() {

    private lateinit var binding: FragmentBookcaseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookcaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val pagerAdapter = ViewPagerBookCaseAdapter(requireActivity())
        pagerAdapter.addFragment(FavoriteBookFragment())
        pagerAdapter.addFragment(ReadRecentlyFragment())
        pagerAdapter.addFragment(WasDownloadFragment())

        binding.vpBookcase.adapter = pagerAdapter
        binding.vpBookcase.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("ViewPagerFragment", "Page ${position+1}")
            }
        })

        val tabTitles = arrayOf("Yêu thích", "Đọc gần đây", "Đã tải về")
        TabLayoutMediator(binding.tabBookcase, binding.vpBookcase) {
                tab, position -> tab.text = tabTitles[position]
        }.attach()
    }
}