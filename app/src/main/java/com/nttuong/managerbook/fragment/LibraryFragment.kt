package com.nttuong.managerbook.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttuong.managerbook.R
import com.nttuong.managerbook.adapter.LibraryAdapter
import com.nttuong.managerbook.databinding.FragmentLibraryBinding
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private lateinit var adapter: LibraryAdapter
    private val viewModel: BookManagerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(layoutInflater)

        adapter = LibraryAdapter()
        binding.rcListCompleteBook.adapter = adapter
        binding.rcListCompleteBook.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.getAllBookList.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapter.submitList(viewModel.getCompleteBook(it))
            }
        }

        return binding.root
    }
}