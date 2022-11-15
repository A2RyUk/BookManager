package com.nttuong.managerbook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttuong.managerbook.R
import com.nttuong.managerbook.adapter.BookManagerAdapter
import com.nttuong.managerbook.databinding.ActivityCompleteListBookBinding
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class CompleteListBook : AppCompatActivity(),
    BookManagerAdapter.OnClickItemListener{

    private lateinit var binding: ActivityCompleteListBookBinding
    private lateinit var viewModel: BookManagerViewModel
    private lateinit var completeBookAdapter: BookManagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteListBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application))
            .get(BookManagerViewModel::class.java)

        completeBookAdapter = BookManagerAdapter(this)
        binding.rcCompleteBook.adapter = completeBookAdapter
        binding.rcCompleteBook.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel.getAllBookList.observe(this) { list ->
            list?.let {
                completeBookAdapter.submitList(viewModel.getCompleteBook(it))
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

    override fun itemClick(book: Book) {
        val detailBookIntent = Intent(this, DetailBookActivity::class.java)
        detailBookIntent.putExtra("itemClickBookID", book.bookId.toString())
        detailBookIntent.putExtra("itemClickAvatar", book.avatar)
        detailBookIntent.putExtra("itemClickName", book.name)
        detailBookIntent.putExtra("itemClickAuthor", book.author)
        detailBookIntent.putExtra("itemClickCategory", book.category)
        detailBookIntent.putExtra("itemClickStatus", book.status)
        detailBookIntent.putExtra("itemClickContent", book.content)
        startActivity(detailBookIntent)
    }
}