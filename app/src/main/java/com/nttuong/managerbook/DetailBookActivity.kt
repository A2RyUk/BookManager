package com.nttuong.managerbook

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.nttuong.managerbook.databinding.AcitivityDetailBookBinding
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.viewmodel.BookViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailBookActivity : AppCompatActivity() {

    private lateinit var binding: AcitivityDetailBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AcitivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateUI()
    }

    private fun updateUI() {
            binding.tvBookName.text = intent.getStringExtra("itemClickName")
            binding.tvAuthor.text = intent.getStringExtra("itemClickAuthor")
            binding.tvStatus.text = intent.getStringExtra("itemClickStatus")
            binding.tvCategory.text = intent.getStringExtra("itemClickCategory")
            binding.tvContent.text = intent.getStringExtra("itemClickContent")
            Glide.with(binding.imgAva)
                .load(intent.getStringExtra("itemClickAvatar"))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(binding.imgAva)
    }
}