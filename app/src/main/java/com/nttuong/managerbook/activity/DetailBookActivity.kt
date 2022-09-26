package com.nttuong.managerbook.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.AcitivityDetailBookBinding


class DetailBookActivity : AppCompatActivity() {

    private lateinit var binding: AcitivityDetailBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AcitivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateUI()

        var bookName = intent.getStringExtra("itemClickName")

        binding.btnListChap.setOnClickListener {
            val intentListChapter = Intent(this, ListChapterActivity::class.java)
            intentListChapter.putExtra("bookNameForChapter", bookName)
            startActivity(intentListChapter)
        }
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