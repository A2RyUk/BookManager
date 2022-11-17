package com.nttuong.managerbook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttuong.managerbook.R
import com.nttuong.managerbook.adapter.BookManagerAdapter
import com.nttuong.managerbook.databinding.ActivityMostFavoriteBookBinding
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class MostFavoriteBook : AppCompatActivity(),
    BookManagerAdapter.OnClickItemListener{

    private lateinit var binding: ActivityMostFavoriteBookBinding
    private lateinit var adapter: BookManagerAdapter
    private lateinit var viewModel: BookManagerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMostFavoriteBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        adapter = BookManagerAdapter(this)
        binding.rcMostFavorite.adapter = adapter
        binding.rcMostFavorite.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application))
            .get(BookManagerViewModel::class.java)

        viewModel.getAllMostFavoriteBook.observe(this) { list->
            list?.let {
                adapter.submitList(it)
            }
        }
    }

    override fun itemClick(book: Book) {
        val detailMostFavoriteBookIntent = Intent(this, DetailBookActivity::class.java)
        detailMostFavoriteBookIntent.putExtra("itemClickBookID", book.bookId.toString())
        detailMostFavoriteBookIntent.putExtra("itemClickAvatar", book.avatar)
        detailMostFavoriteBookIntent.putExtra("itemClickName", book.name)
        detailMostFavoriteBookIntent.putExtra("itemClickAuthor", book.author)
        detailMostFavoriteBookIntent.putExtra("itemClickCategory", book.category)
        detailMostFavoriteBookIntent.putExtra("itemClickStatus", book.status)
        detailMostFavoriteBookIntent.putExtra("itemClickContent", book.content)
        startActivity(detailMostFavoriteBookIntent)
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