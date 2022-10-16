package com.nttuong.managerbook.activity.manager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttuong.managerbook.activity.DetailBookActivity
import com.nttuong.managerbook.adapter.BookManagerAdapter
import com.nttuong.managerbook.databinding.ActivityManagerBookBinding
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.fragment.book.AddBookDialog
import com.nttuong.managerbook.fragment.book.EditBookDialog
import com.nttuong.managerbook.helper.SwipeHelper
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class ManagerBookActivity : FragmentActivity(),
    AddBookDialog.AddBookDialogListener,
    EditBookDialog.EditBookDialogListener,
    BookManagerAdapter.OnClickItemListener {
    private lateinit var binding: ActivityManagerBookBinding
    private lateinit var managerBookAdapter: BookManagerAdapter
    private lateinit var viewModel: BookManagerViewModel

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityManagerBookBinding.inflate(layoutInflater)
            setContentView(binding.root)

            managerBookAdapter = BookManagerAdapter(this)
            binding.rcListBook.adapter = managerBookAdapter
            binding.rcListBook.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application))
                .get(BookManagerViewModel::class.java)
            viewModel.getAllBookList.observe(this) {
                managerBookAdapter.submitList(it)
            }
            binding.btnAdd.setOnClickListener {
                val dialog = AddBookDialog()
                dialog.show(supportFragmentManager, "addBookDialog")
            }

            val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.rcListBook) {
                override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                    var buttons = listOf<UnderlayButton>()
                    buttons = listOf(deleteButton(position), editButton(position))
                    return buttons
                }
            })
            itemTouchHelper.attachToRecyclerView(binding.rcListBook)
    }

    override fun onAddBookDialogPositiveClick(book: Book?) {
        if (book != null) {
            viewModel.insertBook(book)
        } else {
            Toast.makeText(this, "Don have Book to add", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAddBookDialogNegativeClick(book: Book?) {
        Toast.makeText(this, "You click cancel", Toast.LENGTH_SHORT).show()
    }

    private fun deleteButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    val book: Book? = viewModel.getAllBookList.value?.get(position)
                    if (book != null) {
                        viewModel.deleteBook(book.bookId!!)
                    }
                }
            })
    }

    private fun editButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Edit",
            14.0f,
            android.R.color.holo_green_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    val book: Book? = viewModel.getAllBookList.value?.get(position)
                    val dialog = EditBookDialog(book!!)
                    dialog.show(supportFragmentManager, "editBookDialog")
                }
            })
    }

    override fun onEditBookDialogPositiveClick(book: Book?) {
        if (book != null) {
            viewModel.editBook(book)
        } else {
            Toast.makeText(this, "Don have Book to edit", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onEditBookDialogNegativeClick(book: Book?) {
        Toast.makeText(this, "You click cancel", Toast.LENGTH_SHORT).show()
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