package com.nttuong.managerbook.fragment.book

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttuong.managerbook.activity.DetailBookActivity
import com.nttuong.managerbook.R
import com.nttuong.managerbook.adapter.BookAdapter
import com.nttuong.managerbook.adapter.ItemClickListener
import com.nttuong.managerbook.databinding.FragmentBookBinding
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class BookFragment : Fragment(),
    AddBookDialog.AddBookDialogListener,
    DeleteBookDialog.DeleteBookDialogListener,
    EditBookDialog.EditBookDialogListener,
    ItemClickListener {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }
    private var clicked = false

    private lateinit var binding: FragmentBookBinding
    lateinit var viewModel: BookManagerViewModel
    private lateinit var adapter: BookAdapter
    private val books = arrayListOf<Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookBinding.inflate(layoutInflater)

        binding.btnMenu.setOnClickListener {
            onShowButtonClicked()
        }
        binding.btnAdd.setOnClickListener {
            val dialog = AddBookDialog()
            dialog.show(childFragmentManager, "addBookDialog")
        }
        binding.btnEdit.setOnClickListener {
            val dialog = EditBookDialog()
            dialog.show(childFragmentManager, "editBookDialog")
        }
        binding.btnDelete.setOnClickListener {
            val dialog = DeleteBookDialog()
            dialog.show(childFragmentManager, "deleteBookDialog")
        }

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[BookManagerViewModel::class.java]
        viewModel.getAllBookList.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapter.updateList(it)
            }
        }

        adapter = BookAdapter(books, this)
        binding.rcBook.adapter = adapter
        binding.rcBook.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        return binding.root
    }



    //set button
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



    //add book
    override fun onAddBookDialogPositiveClick(book: Book?) {
        if (book != null) {
            viewModel.insertBookAndUpdateAuthorAndCategory(book)
        } else {
            Toast.makeText(requireContext(), "Don have Book to add", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onAddBookDialogNegativeClick(book: Book?) {
        Toast.makeText(requireContext(), "You click cancel", Toast.LENGTH_SHORT).show()
    }


    //delete book
    override fun onDeleteBookDialogPositiveClick(bookId: Int?) {
        if (bookId != null) {
            val deleteIndex = books.indexOf(Book(bookId))
            viewModel.deleteBookAndUpdateAuthorAndCategory(books[deleteIndex])
        } else {
            Toast.makeText(requireContext(), "Don have Book to delete", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDeleteBookDialogNegativeClick(bookId: Int?) {
        Toast.makeText(requireContext(), "You click cancel", Toast.LENGTH_SHORT).show()
    }


    //edit book
    override fun onEditBookDialogPositiveClick(book: Book?) {
        if (book != null) {
            viewModel.editBookAndUpdateAuthorAndCategory(book)
        } else {
            Toast.makeText(requireContext(), "Don have Book to add", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onEditBookDialogNegativeClick(book: Book?) {
        Toast.makeText(requireContext(), "You click cancel", Toast.LENGTH_SHORT).show()
    }


    override fun itemClick(book: Book) {
        val detailBookIntent = Intent(requireContext(), DetailBookActivity::class.java)
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