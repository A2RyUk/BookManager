package com.nttuong.managerbook.activity.manager

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.nttuong.managerbook.activity.ReadingBookActivity
import com.nttuong.managerbook.adapter.ChapterItemClickListener
import com.nttuong.managerbook.adapter.ListChapterAdapter
import com.nttuong.managerbook.databinding.ActivityListChapterBinding
import com.nttuong.managerbook.db.entities.Chapter
import com.nttuong.managerbook.fragment.chapter.AddChapterDialog
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class ListChapterActivity : AppCompatActivity(),
    AddChapterDialog.AddChapterDialogListener,
    ChapterItemClickListener {
    private lateinit var binding: ActivityListChapterBinding
    private lateinit var viewModel: BookManagerViewModel
    private val chapterAdapter = ListChapterAdapter(this)
    private val fAuth = FirebaseAuth.getInstance()
    private val fStore = FirebaseFirestore.getInstance()
    private val userUid = fAuth.currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListChapterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (userUid != null) {
            checkUserAccessLevel(userUid)
        } else {
            binding.btnAdd.visibility = View.GONE
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAddChapterPositiveClick(chapter: Chapter?) {
       if (chapter != null) {
           var bookName = intent.getStringExtra("bookNameForChapter")
           viewModel.chapterInsert(chapter, bookName!!)
           viewModel.updateTimeWhenAddChapter(bookName)
       } else {
           Toast.makeText(this, "Don have chapter to add", Toast.LENGTH_SHORT).show()
       }
    }

    override fun onAddChapterNegativeClick(chapter: Chapter?) {
        Toast.makeText(this, "You click cancel", Toast.LENGTH_SHORT).show()
    }

    override fun itemClick(chapter: Chapter) {
        val userUID = fAuth.currentUser!!.uid
        if (userUID.isNotEmpty()) {
            val df = fStore.collection("User").document(userUID)
                .collection("ListBook").document(chapter.bookName.toString())
            df.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        if (document.get("ReadToChap") != null) {
                            val readToOnSever = document.getString("ReadToChap").toString().toInt()
                            if (readToOnSever < chapter.chapNumber!!) {
                                val readTo = hashMapOf("ReadToChap" to "${chapter.chapNumber}")
                                df.set(readTo, SetOptions.merge())
                            }
                        } else {
                            val readTo = hashMapOf("ReadToChap" to "${chapter.chapNumber}")
                            df.set(readTo, SetOptions.merge())
                        }
                    } else {
                        val readTo = hashMapOf("ReadToChap" to "${chapter.chapNumber}")
                        df.set(readTo, SetOptions.merge())
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
                            viewModel.addNewAndDeleteOldBook(readRecentDB, document, chapter.bookName)
                        } else {
                            viewModel.addAndCreateField(readRecentDB, chapter.bookName)
                        }
                    } else {
                        viewModel.addAndCreateField(readRecentDB, chapter.bookName)
                    }
                }
            }
        }
        val readingIntent = Intent(this, ReadingBookActivity::class.java)
        readingIntent.putExtra("chapterID", chapter.chapterId.toString())
        readingIntent.putExtra("chapterName", chapter.chapName)
        readingIntent.putExtra("chapterNumber", chapter.chapNumber.toString())
        readingIntent.putExtra("chapterContent", chapter.content)
        readingIntent.putExtra("chapterBookName", chapter.bookName)
        startActivity(readingIntent)
    }

    private fun checkUserAccessLevel(uid: String) {
        val df: DocumentReference = fStore.collection("User").document(uid)
        df.get().addOnSuccessListener {
            if (it.getString("IsAdmin") == "isAdmin") {
                binding.btnAdd.visibility = View.VISIBLE
            } else if (it.getString("IsAdmin") == "isUser") {
                binding.btnAdd.visibility = View.GONE
            }
        }
    }
}