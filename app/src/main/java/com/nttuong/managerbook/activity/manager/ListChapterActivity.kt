package com.nttuong.managerbook.activity.manager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttuong.managerbook.activity.ReadingBookActivity
import com.nttuong.managerbook.adapter.ChapterItemClickListener
import com.nttuong.managerbook.adapter.ListChapterAdapter
import com.nttuong.managerbook.databinding.ActivityListChapterBinding
import com.nttuong.managerbook.db.entities.Chapter
import com.nttuong.managerbook.fragment.chapter.AddChapterDialog
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class ListChapterActivity : FragmentActivity(),
    AddChapterDialog.AddChapterDialogListener,
    ChapterItemClickListener {
    private lateinit var binding: ActivityListChapterBinding
    private lateinit var viewModel: BookManagerViewModel
    private val chapterAdapter = ListChapterAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListChapterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            val dialog = AddChapterDialog()
            dialog.show(supportFragmentManager, "addChapterDialog")
        }

        binding.rvChapter.adapter = chapterAdapter
        binding.rvChapter.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(BookManagerViewModel::class.java)

        val name = intent.getStringExtra("bookNameForChapter")
        if (name != null) {
            viewModel.getAllChaptersByName(name).observe(this) { list ->
                list?.let {
                    chapterAdapter.submitList(it)
                }
            }
        }
    }

    override fun onAddChapterPositiveClick(chapter: Chapter?) {
       if (chapter != null) {
           var bookName = intent.getStringExtra("bookNameForChapter")
           viewModel.chapterInsert(chapter, bookName!!)
       } else {
           Toast.makeText(this, "Don have chapter to add", Toast.LENGTH_SHORT).show()
       }
    }

    override fun onAddChapterNegativeClick(chapter: Chapter?) {
        Toast.makeText(this, "You click cancel", Toast.LENGTH_SHORT).show()
    }

    override fun itemClick(chapter: Chapter) {
        val readingIntent = Intent(this, ReadingBookActivity::class.java)
        readingIntent.putExtra("chapterID", chapter.chapterId.toString())
        readingIntent.putExtra("chapterName", chapter.chapName)
        readingIntent.putExtra("chapterNumber", chapter.chapNumber.toString())
        readingIntent.putExtra("chapterContent", chapter.content)
        readingIntent.putExtra("chapterBookName", chapter.bookName)
        startActivity(readingIntent)
    }
}