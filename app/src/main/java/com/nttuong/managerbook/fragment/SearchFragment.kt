package com.nttuong.managerbook.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttuong.managerbook.R
import com.nttuong.managerbook.activity.DetailBookActivity
import com.nttuong.managerbook.adapter.ChapterItemClickListener
import com.nttuong.managerbook.adapter.SearchBookAdapter
import com.nttuong.managerbook.databinding.FragmentSearchBinding
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.db.entities.Chapter
import com.nttuong.managerbook.viewmodel.BookManagerViewModel


class SearchFragment : Fragment(),
    SearchView.OnQueryTextListener,
    SearchBookAdapter.SearchItemClickListener {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchBookAdapter
    private val viewModel: BookManagerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        adapter = SearchBookAdapter(this)
        binding.rcSearch.adapter = adapter
        binding.rcSearch.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.books.observe(viewLifecycleOwner){ list ->
            list?.let {
                adapter.submitList(it)
            }
        }

        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.toolbar)
        activity!!.supportActionBar?.title = "Tìm Sách"
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val search = menu?.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchData(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchData(query)
        }
        return true
    }

    private fun searchData(query: String) {
        val searchQuery = "%$query"
        viewModel.searchWithText(searchQuery)
    }

    override fun searchItemClick(book: Book) {
        val searchBookIntent = Intent(requireContext(), DetailBookActivity::class.java)
        searchBookIntent.putExtra("itemClickBookID", book.bookId.toString())
        searchBookIntent.putExtra("itemClickAvatar", book.avatar)
        searchBookIntent.putExtra("itemClickName", book.name)
        searchBookIntent.putExtra("itemClickAuthor", book.author)
        searchBookIntent.putExtra("itemClickCategory", book.category)
        searchBookIntent.putExtra("itemClickStatus", book.status)
        searchBookIntent.putExtra("itemClickContent", book.content)
        startActivity(searchBookIntent)
    }
}