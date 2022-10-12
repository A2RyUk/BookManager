package com.nttuong.managerbook.fragment.category


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nttuong.managerbook.adapter.CategoryAdapter
import com.nttuong.managerbook.databinding.FragmentCategoryBinding
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private val categoryAdapter = CategoryAdapter()
    private lateinit var viewModel: BookManagerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
            .get(BookManagerViewModel::class.java)

        binding.rcCategory.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rcCategory.adapter = categoryAdapter

        viewModel.getAllCategoryList.observe(viewLifecycleOwner) {
            categoryAdapter.submitList(it)
        }

        return binding.root
    }
}