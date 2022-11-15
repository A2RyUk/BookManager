package com.nttuong.managerbook.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.nttuong.managerbook.R
import com.nttuong.managerbook.activity.manager.ListChapterActivity
import com.nttuong.managerbook.databinding.AcitivityDetailBookBinding
import com.nttuong.managerbook.db.entities.Chapter
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class DetailBookActivity: AppCompatActivity() {

    private lateinit var binding: AcitivityDetailBookBinding
    private lateinit var viewModel: BookManagerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AcitivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateUI()

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(BookManagerViewModel::class.java)

        var bookName = intent.getStringExtra("itemClickName")
        binding.btnListChap.setOnClickListener {
            val intentListChapter = Intent(this, ListChapterActivity::class.java)
            intentListChapter.putExtra("bookNameForChapter", bookName)
            startActivity(intentListChapter)
        }
        if (bookName != null) {
            viewModel.getAllChaptersByName(bookName).observe(this) { list ->
                list?.let {
                    updateNewChapter(it)
                }
            }
        }
    }

    private fun updateNewChapter(listChapter: List<Chapter>) {
        if (listChapter.isNotEmpty()) {
            binding.tvChapNumber.text = listChapter[listChapter.size - 1].chapNumber.toString()
            binding.tvChapName.text = listChapter[listChapter.size - 1].chapName.toString()
        } else {
            binding.tvChapNumber.text = "0"
            binding.tvChapName.text = ""
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}