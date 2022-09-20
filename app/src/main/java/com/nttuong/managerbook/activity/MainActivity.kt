package com.nttuong.managerbook.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.nttuong.managerbook.adapter.ViewPagerAdapter
import com.nttuong.managerbook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tabTitles = arrayOf("Sách", "Thể Loại", "Tác Giả")
        binding.vpBook.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabMain, binding.vpBook){
                tab, position -> tab.text = tabTitles[position]
        }.attach()
    }

}