package com.nttuong.managerbook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttuong.managerbook.R
import com.nttuong.managerbook.adapter.BookManagerAdapter
import com.nttuong.managerbook.databinding.ActivityMostViewBookBinding
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class MostViewBook : AppCompatActivity(),
    BookManagerAdapter.OnClickItemListener {

    private lateinit var binding: ActivityMostViewBookBinding
    private lateinit var adapter: BookManagerAdapter
    private lateinit var viewModel: BookManagerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMostViewBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        adapter = BookManagerAdapter(this)
        binding.rcMostView.adapter = adapter
        binding.rcMostView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application))
            .get(BookManagerViewModel::class.java)

        viewModel.getAllMostViewBook.observe(this) { list->
            list?.let {
                adapter.submitList(it)
            }
        }
    }

    override fun itemClick(book: Book) {
        val detailMostViewBookIntent = Intent(this, DetailBookActivity::class.java)
        detailMostViewBookIntent.putExtra("itemClickBookID", book.bookId.toString())
        detailMostViewBookIntent.putExtra("itemClickAvatar", book.avatar)
        detailMostViewBookIntent.putExtra("itemClickName", book.name)
        detailMostViewBookIntent.putExtra("itemClickAuthor", book.author)
        detailMostViewBookIntent.putExtra("itemClickCategory", book.category)
        detailMostViewBookIntent.putExtra("itemClickStatus", book.status)
        detailMostViewBookIntent.putExtra("itemClickContent", book.content)
        startActivity(detailMostViewBookIntent)
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