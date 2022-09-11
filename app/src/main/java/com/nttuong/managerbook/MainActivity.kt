package com.nttuong.managerbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.nttuong.managerbook.adapter.ViewPagerAdapter
import com.nttuong.managerbook.databinding.ActivityMainBinding
import com.nttuong.managerbook.viewmodel.BookViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tabTitles = arrayOf("Book", "Category", "Author")
        binding.vpBook.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabMain, binding.vpBook){
                tab, position -> tab.text = tabTitles[position]
        }.attach()
    }

}