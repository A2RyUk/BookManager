package com.nttuong.managerbook.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttuong.managerbook.activity.*
import com.nttuong.managerbook.adapter.CustomAdapter
import com.nttuong.managerbook.databinding.FragmentLibraryBinding
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class LibraryFragment : Fragment(), CustomAdapter.CustomAdapterListener {
    private lateinit var binding: FragmentLibraryBinding
    private lateinit var adapterCompleteBook: CustomAdapter
    private lateinit var adapterUpdateBook: CustomAdapter
    private lateinit var adapterPostBook: CustomAdapter
    private val viewModel: BookManagerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(layoutInflater)

        adapterCompleteBook = CustomAdapter(this)
        adapterUpdateBook = CustomAdapter(this)
        adapterPostBook = CustomAdapter(this)
        binding.rcListCompleteBook.adapter = adapterCompleteBook
        binding.rcListCompleteBook.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.getAllBookList.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapterCompleteBook.updateList(viewModel.getCompleteBook(it))
            }
        }

        binding.rcListUpdateBook.adapter = adapterUpdateBook
        binding.rcListUpdateBook.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.getAllBookByUpdateDate.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapterUpdateBook.updateList(it)
            }
        }

        binding.rcListNewBook.adapter = adapterPostBook
        binding.rcListNewBook.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.getAllBooksByPostDate.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapterPostBook.updateList(it)
            }
        }

        binding.btnSeeNewBook.setOnClickListener {
            val newBookIntent = Intent(requireContext(), NewPostListBook::class.java)
            startActivity(newBookIntent)
        }

        binding.btnSeeNewUpdate.setOnClickListener {
            val updateBookIntent = Intent(requireContext(), UpdateListBook::class.java)
            startActivity(updateBookIntent)
        }

        binding.btnSeeComplete.setOnClickListener {
            val completeBookIntent = Intent(requireContext(), CompleteListBook::class.java)
            startActivity(completeBookIntent)
        }
        binding.constraintBtnView.setOnClickListener {
            val viewBookIntent = Intent(requireContext(), MostViewBook::class.java)
            startActivity(viewBookIntent)
        }
        binding.constraintBtnLike.setOnClickListener {
            val favoriteBookIntent = Intent(requireContext(), MostFavoriteBook::class.java)
            startActivity(favoriteBookIntent)
        }
        binding.constraintBtnComment.setOnClickListener {
            Toast.makeText(requireContext(), "Hiện Tại App Đang sử dụng database OffLine nên không phát triển tính năng này!", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onItemClick(book: Book) {
        val completeBookIntent = Intent(requireContext(), DetailBookActivity::class.java)
        completeBookIntent.putExtra("itemClickBookID", book.bookId.toString())
        completeBookIntent.putExtra("itemClickAvatar", book.avatar)
        completeBookIntent.putExtra("itemClickName", book.name)
        completeBookIntent.putExtra("itemClickAuthor", book.author)
        completeBookIntent.putExtra("itemClickCategory", book.category)
        completeBookIntent.putExtra("itemClickStatus", book.status)
        completeBookIntent.putExtra("itemClickContent", book.content)
        startActivity(completeBookIntent)
    }
}