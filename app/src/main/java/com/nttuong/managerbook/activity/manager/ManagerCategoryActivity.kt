package com.nttuong.managerbook.activity.manager

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttuong.managerbook.adapter.CategoryAdapter
import com.nttuong.managerbook.adapter.CategoryManagerAdapter
import com.nttuong.managerbook.databinding.ActivityManagerCategoryBinding
import com.nttuong.managerbook.db.entities.Category
import com.nttuong.managerbook.fragment.category.AddCategoryDialog
import com.nttuong.managerbook.helper.SwipeHelper
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class ManagerCategoryActivity : FragmentActivity(), AddCategoryDialog.AddCategoryDialogListener {

    private lateinit var binding: ActivityManagerCategoryBinding
    private val categoryAdapter = CategoryManagerAdapter()
    private lateinit var viewModel: BookManagerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application))
            .get(BookManagerViewModel::class.java)

        binding.rcManagerCategory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcManagerCategory.adapter = categoryAdapter

        viewModel.getAllCategoryList.observe(this) {
            categoryAdapter.submitList(it)
        }

        binding.btnAdd.setOnClickListener {
            val dialog = AddCategoryDialog()
            dialog.show(supportFragmentManager, "addCategoryDialog")
        }

        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.rcManagerCategory) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                var buttons = listOf<UnderlayButton>()
                buttons = listOf(deleteButton(position))
                return buttons
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rcManagerCategory)
    }

    private fun deleteButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    val category: Category? = viewModel.getAllCategoryList.value?.get(position)
                    if (category != null) {
                        viewModel.deleteCategory(category.categoryId!!)
                    }
                }
            })
    }

    override fun onAddCategoryDialogPositiveClick(category: Category?) {
        if (category != null) {
            viewModel.insertCategory(category)
        } else {
            Toast.makeText(this, "Don have Category to add", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAddCategoryDialogNegativeClick(category: Category?) {
        Toast.makeText(this, "You click cancel", Toast.LENGTH_SHORT).show()
    }
}