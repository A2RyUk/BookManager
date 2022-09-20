package com.nttuong.managerbook.fragment.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nttuong.managerbook.R
import com.nttuong.managerbook.adapter.CategoryAdapter
import com.nttuong.managerbook.databinding.FragmentCategoryBinding
import com.nttuong.managerbook.db.entities.Category
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class CategoryFragment : Fragment(),
    AddCategoryDialog.AddCategoryDialogBinding,
    DeleteCategoryDialog.DeleteCategoryDialogBinding,
    EditCategoryDialog.EditCategoryDialogBinding {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }
    private var clicked = false

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var adapter: CategoryAdapter
    private val categories = arrayListOf<Category>()
    private lateinit var viewModel: BookManagerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        binding.btnMenu.setOnClickListener {
            onShowButtonClicked()
        }
        binding.btnAdd.setOnClickListener {
            val dialog = AddCategoryDialog()
            dialog.show(childFragmentManager, "addCategoryDialog")
        }
        binding.btnEdit.setOnClickListener {
            val dialog = EditCategoryDialog()
            dialog.show(childFragmentManager, "editCategoryDialog")
        }
        binding.btnDelete.setOnClickListener {
            val dialog = DeleteCategoryDialog()
            dialog.show(childFragmentManager, "deleteCategoryDialog")
        }

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
            .get(BookManagerViewModel::class.java)
        viewModel.getAllCategoryList.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapter.updateList(it)
            }
        }

        adapter = CategoryAdapter(categories)
        binding.rcCategory.adapter = adapter
        binding.rcCategory.layoutManager =
            GridLayoutManager(requireContext(), 3)

        return binding.root
    }

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


    //add
    override fun onAddCategoryDialogPositiveClick(category: Category?) {
        if (category != null) {
            viewModel.getBookNumberAndInsertCategory(category)
        } else {
            Toast.makeText(requireContext(), "Don have Category to add", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onAddCategoryDialogNegativeClick(category: Category?) {
        Toast.makeText(requireContext(), "You click cancel", Toast.LENGTH_SHORT).show()
    }


    //delete
    override fun onDeleteCategoryDialogPositiveClick(categoryId: Int?) {
        if (categoryId != null) {
            val deleteIndex = categories.indexOf(Category(categoryId))
            viewModel.categoryDelete(categories[deleteIndex])
        } else {
            Toast.makeText(requireContext(), "Don have Category to delete", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDeleteCategoryDialogNegativeClick(categoryId: Int?) {
        Toast.makeText(requireContext(), "You click cancel", Toast.LENGTH_SHORT).show()
    }


    //edit
    override fun onEditCategoryDialogPositiveClick(category: Category?) {
        if (category != null) {
            viewModel.categoryUpdate(category)
        } else {
            Toast.makeText(requireContext(), "Don have Category to update", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onEditCategoryDialogNegativeClick(category: Category?) {
        Toast.makeText(requireContext(), "You click cancel", Toast.LENGTH_SHORT).show()
    }


}