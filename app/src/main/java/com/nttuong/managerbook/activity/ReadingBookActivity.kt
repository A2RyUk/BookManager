package com.nttuong.managerbook.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.nttuong.managerbook.databinding.ActivityReadingBookBinding
import com.nttuong.managerbook.db.entities.Chapter
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class ReadingBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReadingBookBinding
    private var touch = false
    private lateinit var viewModel: BookManagerViewModel
    private lateinit var chapName: String
    private lateinit var bookName: String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadingBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateUI(getChapterFromIntent())

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(BookManagerViewModel::class.java)

        binding.readingLayout.setOnTouchListener { v, event ->
            showAndHide(touch)
            false
        }

        binding.bottomMenu.btnNext.setOnClickListener {
            Log.d("chap name", chapName)
            Log.d("book name", bookName)
            viewModel.getAllChapters.observe(this) { list ->
                list?.let {
                    updateUI(viewModel.getNextChapter(chapName, bookName, it))
                }
            }
        }
        binding.bottomMenu.btnPrev.setOnClickListener {

        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(chapter: Chapter) {
        binding.tvChapNumberAndName.text = "Chương " + chapter.chapNumber + ": " + chapter.chapName
        binding.tvChapContent.text = chapter.content
        binding.topMenu.txtBookName.text = chapter.bookName
        chapName = chapter.chapName.toString()
        bookName = chapter.bookName.toString()
    }

    private fun getChapterFromIntent(): Chapter {
        var id = intent.getStringExtra("chapterID").toString().toInt()
        var chapName = intent.getStringExtra("chapterName").toString()
        var chapNumber = intent.getStringExtra("chapterNumber").toString().toInt()
        var content = intent.getStringExtra("chapterContent").toString()
        var bookName = intent.getStringExtra("chapterBookName").toString()
        return Chapter(
            chapterId = id,
            chapName = chapName,
            chapNumber = chapNumber,
            content = content,
            bookName = bookName
        )
    }

    private fun showAndHide(boolean: Boolean) {
        if (!boolean) {
            binding.topMenu.llTopMenu.visibility = View.GONE
            binding.bottomMenu.llBottomMenu.visibility = View.GONE
        } else {
            binding.topMenu.llTopMenu.visibility = View.VISIBLE
            binding.bottomMenu.llBottomMenu.visibility = View.VISIBLE
        }
        touch = !touch
    }
}