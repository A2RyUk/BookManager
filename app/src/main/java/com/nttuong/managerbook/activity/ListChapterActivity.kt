package com.nttuong.managerbook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttuong.managerbook.R
import com.nttuong.managerbook.adapter.BookAdapter
import com.nttuong.managerbook.adapter.ChapterAdapter
import com.nttuong.managerbook.adapter.ChapterItemClickListener
import com.nttuong.managerbook.databinding.ActivityListChapterBinding
import com.nttuong.managerbook.db.entities.Category
import com.nttuong.managerbook.db.entities.Chapter
import com.nttuong.managerbook.fragment.chapter.AddChapterDialog
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class ListChapterActivity : FragmentActivity(),
    AddChapterDialog.AddChapterDialogListener,
    ChapterItemClickListener {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.rotate_open_anim
    ) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.rotate_close_anim
    ) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.from_bottom_anim
    ) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.to_bottom_anim
    ) }
    private var clicked = false

    private lateinit var binding: ActivityListChapterBinding
    private lateinit var viewModel: BookManagerViewModel
    private val chapterAdapter = ChapterAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListChapterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMenu.setOnClickListener {
            onShowButtonClicked()
        }
        binding.btnAdd.setOnClickListener {
            val dialog = AddChapterDialog()
            dialog.show(supportFragmentManager, "AddChapterDialog")
        }
        binding.btnEdit.setOnClickListener {

        }
        binding.btnDelete.setOnClickListener {

        }

        binding.rvChapter.adapter = chapterAdapter
        binding.rvChapter.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(BookManagerViewModel::class.java)

        var bookName = intent.getStringExtra("bookNameForChapter")
        viewModel.getAllChapters.observe(this) { list ->
            list?.let {
                chapterAdapter.updateUI(viewModel.getListChaptersByBookName(bookName.toString(), it))
            }
        }

    }

    private fun onShowButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if(!clicked) {
            binding.btnAdd.visibility = View.VISIBLE
            binding.btnEdit.visibility = View.VISIBLE
            binding.btnDelete.visibility = View.VISIBLE
        } else {
            binding.btnAdd.visibility = View.INVISIBLE
            binding.btnEdit.visibility = View.INVISIBLE
            binding.btnDelete.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked) {
            binding.btnAdd.startAnimation(fromBottom)
            binding.btnEdit.startAnimation(fromBottom)
            binding.btnDelete.startAnimation(fromBottom)
            binding.btnMenu.startAnimation(rotateOpen)
        } else {
            binding.btnAdd.startAnimation(toBottom)
            binding.btnEdit.startAnimation(toBottom)
            binding.btnDelete.startAnimation(toBottom)
            binding.btnMenu.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if(!clicked) {
            binding.btnAdd.isClickable = true
            binding.btnEdit.isClickable = true
            binding.btnDelete.isClickable = true
        } else {
            binding.btnAdd.isClickable = false
            binding.btnEdit.isClickable = false
            binding.btnDelete.isClickable = false
        }
    }

    override fun onAddChapterPositiveClick(chapter: Chapter?) {
        var bookName = intent.getStringExtra("bookNameForChapter")
        if (chapter != null) {
            viewModel.chapterInsert(chapter, bookName.toString())
        } else {
            Toast.makeText(this, "Don have Category to delete", Toast.LENGTH_SHORT).show()
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