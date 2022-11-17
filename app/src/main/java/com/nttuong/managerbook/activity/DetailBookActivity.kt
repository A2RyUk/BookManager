package com.nttuong.managerbook.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.nttuong.managerbook.R
import com.nttuong.managerbook.activity.manager.ListChapterActivity
import com.nttuong.managerbook.databinding.AcitivityDetailBookBinding
import com.nttuong.managerbook.db.entities.Chapter
import com.nttuong.managerbook.viewmodel.BookManagerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailBookActivity: AppCompatActivity() {

    private lateinit var binding: AcitivityDetailBookBinding
    private lateinit var viewModel: BookManagerViewModel
    private val fAuth = FirebaseAuth.getInstance()
    private val fStore = FirebaseFirestore.getInstance()

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

        binding.btnRead.setOnClickListener {
            viewModel.getAllChaptersByName(bookName!!).observe(this) { list ->
                list?.let {
                    if (it.isNotEmpty()) {
                        val userUID = fAuth.currentUser!!.uid
                        if (userUID.isNotEmpty()) {
                            val df = fStore.collection("User").document(userUID)
                                .collection("ListBook").document(bookName.toString())
                            df.get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val document = task.result
                                    if (document.exists()) {
                                        if (document.get("ReadToChap") != null) {
                                            var readToChapter = document.getString("ReadToChap").toString().toInt()
                                            lifecycleScope.launch (Dispatchers.IO) {
                                                var chapter = viewModel.getReadToChapter(readToChapter)
                                                val readingIntent = Intent(this@DetailBookActivity, ReadingBookActivity::class.java)
                                                readingIntent.putExtra("chapterID", chapter.chapterId.toString())
                                                readingIntent.putExtra("chapterName", chapter.chapName)
                                                readingIntent.putExtra("chapterNumber", chapter.chapNumber.toString())
                                                readingIntent.putExtra("chapterContent", chapter.content)
                                                readingIntent.putExtra("chapterBookName", chapter.bookName)
                                                startActivity(readingIntent)
                                            }
                                        } else {
                                            val readTo = hashMapOf("ReadToChap" to "1")
                                            df.set(readTo, SetOptions.merge())
                                            lifecycleScope.launch (Dispatchers.IO) {
                                                var chapter = viewModel.getReadToChapter(1)
                                                val readingIntent = Intent(this@DetailBookActivity, ReadingBookActivity::class.java)
                                                readingIntent.putExtra("chapterID", chapter.chapterId.toString())
                                                readingIntent.putExtra("chapterName", chapter.chapName)
                                                readingIntent.putExtra("chapterNumber", chapter.chapNumber.toString())
                                                readingIntent.putExtra("chapterContent", chapter.content)
                                                readingIntent.putExtra("chapterBookName", chapter.bookName)
                                                startActivity(readingIntent)
                                            }
                                        }
                                    } else {
                                        val readTo = hashMapOf("ReadToChap" to "1")
                                        df.set(readTo, SetOptions.merge())
                                        lifecycleScope.launch (Dispatchers.IO) {
                                            var chapter = viewModel.getReadToChapter(1)
                                            val readingIntent = Intent(this@DetailBookActivity, ReadingBookActivity::class.java)
                                            readingIntent.putExtra("chapterID", chapter.chapterId.toString())
                                            readingIntent.putExtra("chapterName", chapter.chapName)
                                            readingIntent.putExtra("chapterNumber", chapter.chapNumber.toString())
                                            readingIntent.putExtra("chapterContent", chapter.content)
                                            readingIntent.putExtra("chapterBookName", chapter.bookName)
                                            startActivity(readingIntent)
                                        }
                                    }
                                }
                            }
                            val readRecentDB = fStore.collection("User").document(userUID)
                                .collection("RecentBook").document("ReadRecent")
                            readRecentDB.get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val document = task.result
                                    if (document.exists()) {
                                        if (document.get("Book1") != null) {
                                            viewModel.addNewAndDeleteOldBook(readRecentDB, document, bookName)
                                        } else {
                                            viewModel.addAndCreateField(readRecentDB, bookName)
                                        }
                                    } else {
                                        viewModel.addAndCreateField(readRecentDB, bookName)
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this, "Truyện chưa có chương nào vui lòng chờ update!", Toast.LENGTH_SHORT).show()
                    }
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