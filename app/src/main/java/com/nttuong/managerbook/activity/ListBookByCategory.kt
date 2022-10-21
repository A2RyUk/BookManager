package com.nttuong.managerbook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttuong.managerbook.R
import com.nttuong.managerbook.adapter.BookManagerAdapter
import com.nttuong.managerbook.databinding.ActivityListBookByCategoryBinding
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class ListBookByCategory : AppCompatActivity(),
    BookManagerAdapter.OnClickItemListener {

    private lateinit var binding: ActivityListBookByCategoryBinding
    private val adapter = BookManagerAdapter(this)
    private lateinit var viewModel: BookManagerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBookByCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rcListBookByCategory.adapter = adapter
        binding.rcListBookByCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(BookManagerViewModel::class.java)

        var category = intent.getStringExtra("categoryNameForListBook")
        binding.tvCategory.text = "Truyện $category"
        if (category != null) {
            viewModel.getAllBookByCategory(category).observe(this) { list ->
                list?.let {
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun itemClick(book: Book) {
        val detailBookByCategoryIntent = Intent(this, DetailBookActivity::class.java)
        detailBookByCategoryIntent.putExtra("itemClickBookID", book.bookId.toString())
        detailBookByCategoryIntent.putExtra("itemClickAvatar", book.avatar)
        detailBookByCategoryIntent.putExtra("itemClickName", book.name)
        detailBookByCategoryIntent.putExtra("itemClickAuthor", book.author)
        detailBookByCategoryIntent.putExtra("itemClickCategory", book.category)
        detailBookByCategoryIntent.putExtra("itemClickStatus", book.status)
        detailBookByCategoryIntent.putExtra("itemClickContent", book.content)
        startActivity(detailBookByCategoryIntent)
    }
}