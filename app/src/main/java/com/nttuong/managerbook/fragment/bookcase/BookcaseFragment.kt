package com.nttuong.managerbook.fragment.bookcase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
        binding = FragmentBookcaseBinding.inflate(layoutInflater)

        val tabTitles = arrayOf("Yêu thích", "Đọc gần đây", "Đã tải về")
        binding.vpBookcase.adapter = ViewPagerBookCaseAdapter(requireActivity() as AppCompatActivity)
        TabLayoutMediator(binding.tabBookcase, binding.vpBookcase) {
            tab, position -> tab.text = tabTitles[position]
        }.attach()
        return binding.root
    }
}