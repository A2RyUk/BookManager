package com.nttuong.managerbook.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.nttuong.managerbook.R
import com.nttuong.managerbook.activity.manager.ListChapterActivity
import com.nttuong.managerbook.databinding.ActivityReadingBookBinding
import com.nttuong.managerbook.db.entities.Chapter
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class ReadingBookActivity: AppCompatActivity() {
    private lateinit var binding: ActivityReadingBookBinding
    private var touch = false
    private lateinit var viewModel: BookManagerViewModel
    private lateinit var chapName: String
    private lateinit var bookName: String
    private val fAuth = FirebaseAuth.getInstance()
    private val fStore = FirebaseFirestore.getInstance()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadingBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(BookManagerViewModel::class.java)

        updateUI(getChapterFromIntent())
        binding.readingLayout.setOnTouchListener { v, event ->
            showAndHide(touch)
            false
        }
        binding.topMenu.btnListChap.setOnClickListener {
            val openListChapter = Intent(this, ListChapterActivity::class.java)
            openListChapter.putExtra("bookNameForChapter", bookName)
            startActivity(openListChapter)
        }
        binding.topMenu.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.bottomMenu.btnNext.setOnClickListener {
            viewModel.getAllChaptersByName(bookName).observe(this) { list ->
                list?.let {
                    updateUI(viewModel.getNextChapter(chapName, it))
                }
            }
        }
        binding.bottomMenu.btnPrev.setOnClickListener {
            viewModel.getAllChaptersByName(bookName).observe(this) { list ->
                list?.let {
                    updateUI(viewModel.getPrevChapter(chapName, it))
                }
            }
        }
        binding.bottomMenu.btnFavorite.setOnClickListener {
            val userUID = fAuth.currentUser!!.uid
            var currentFavoriteState = binding.bottomMenu.btnFavorite.drawable.constantState
            var favoriteState = getDrawable(R.drawable.is_favorite)!!.constantState
            if (userUID.isNotEmpty()) {
                val df = fStore.collection("User").document(userUID)
                    .collection("ListBook").document(bookName)
                if (currentFavoriteState == favoriteState) {
                    val updateFavorite = hashMapOf("Favorite" to "no")
                    df.set(updateFavorite, SetOptions.merge())
                    binding.bottomMenu.btnFavorite.setImageResource(R.drawable.not_favorite)
                } else {
                    val updateFavorite = hashMapOf("Favorite" to "yes")
                    df.set(updateFavorite, SetOptions.merge())
                    binding.bottomMenu.btnFavorite.setImageResource(R.drawable.is_favorite)
                }
            } else {
                Toast.makeText(this, "Bạn phải đăng nhập để sử dụng tính năng này!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(chapter: Chapter) {
        binding.tvChapNumberAndName.text = "Chương " + chapter.chapNumber + ": " + chapter.chapName
        binding.tvChapContent.text = chapter.content
        binding.topMenu.txtBookName.text = chapter.bookName
        chapName = chapter.chapName.toString()
        bookName = chapter.bookName.toString()
        val userUID = fAuth.currentUser!!.uid
        if (userUID.isNotEmpty()) {
            val df = fStore.collection("User").document(userUID)
                .collection("ListBook").document(chapter.bookName.toString())
            df.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    val readToOnSever = document.getString("ReadToChap").toString().toInt()
                    if (readToOnSever < chapter.chapNumber!!) {
                        val readTo = hashMapOf("ReadToChap" to "${chapter.chapNumber}")
                        df.set(readTo, SetOptions.merge())
                    }
                    val favorite = document.getString("Favorite")
                    if (favorite == "yes") {
                        binding.bottomMenu.btnFavorite.setImageResource(R.drawable.is_favorite)
                    } else {
                        binding.bottomMenu.btnFavorite.setImageResource(R.drawable.not_favorite)
                    }
                }
            }
        }
        viewModel.getAllChaptersByName(bookName).observe(this) { list ->
            list?.let {
                if (it.size == 1) {
                    binding.bottomMenu.btnNext.visibility = View.GONE
                    binding.bottomMenu.btnPrev.visibility = View.GONE
                } else {
                    if (chapName == it[it.size - 1].chapName) {
                        binding.bottomMenu.btnNext.visibility = View.GONE
                        binding.bottomMenu.btnPrev.visibility = View.VISIBLE
                    } else if (chapName == it[0].chapName) {
                        binding.bottomMenu.btnPrev.visibility = View.GONE
                        binding.bottomMenu.btnNext.visibility = View.VISIBLE
                    } else {
                        binding.bottomMenu.btnPrev.visibility = View.VISIBLE
                        binding.bottomMenu.btnNext.visibility = View.VISIBLE
                    }
                }
            }
        }
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
            bookName = bookName,
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