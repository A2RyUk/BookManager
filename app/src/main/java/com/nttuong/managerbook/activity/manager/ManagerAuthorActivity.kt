package com.nttuong.managerbook.activity.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttuong.managerbook.adapter.AuthorManagerAdapter
import com.nttuong.managerbook.databinding.ActivityManagerAuthorBinding
import com.nttuong.managerbook.db.entities.Author
import com.nttuong.managerbook.fragment.author.AddAuthorDialog
import com.nttuong.managerbook.fragment.author.EditAuthorDialog
import com.nttuong.managerbook.helper.SwipeHelper
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class ManagerAuthorActivity : FragmentActivity(),
    AddAuthorDialog.AddAuthorDialogListener,
    EditAuthorDialog.EditAuthorDialogListener {

    private lateinit var binding: ActivityManagerAuthorBinding
    private lateinit var managerAuthorAdapter: AuthorManagerAdapter
    private lateinit var viewModel: BookManagerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerAuthorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managerAuthorAdapter = AuthorManagerAdapter()
        binding.rcManagerAuthor.adapter = managerAuthorAdapter
        binding.rcManagerAuthor.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application))
            .get(BookManagerViewModel::class.java)
        viewModel.getAllAuthorList.observe(this) {
            managerAuthorAdapter.submitList(it)
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnAdd.setOnClickListener {
            val dialog = AddAuthorDialog()
            dialog.show(supportFragmentManager, "addAuthorDialog")
        }

        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.rcManagerAuthor) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                var buttons = listOf<UnderlayButton>()
                buttons = listOf(deleteButton(position), editButton(position))
                return buttons
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rcManagerAuthor)
    }

    private fun deleteButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    val author: Author? = viewModel.getAllAuthorList.value?.get(position)
                    if (author != null) {
                        viewModel.deleteAuthor(author.authorId!!)
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
                    val author: Author? = viewModel.getAllAuthorList.value?.get(position)
                    val dialog = EditAuthorDialog(author!!)
                    dialog.show(supportFragmentManager, "editAuthorDialog")
                }
            })
    }

    override fun onAddAuthorDialogPositiveClick(author: Author?) {
        if (author != null) {
            viewModel.insertAuthor(author)
        } else {
            Toast.makeText(this, "Don have Author to add", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAddAuthorDialogNegativeClick(author: Author?) {
        Toast.makeText(this, "You click cancel", Toast.LENGTH_SHORT).show()
    }

    override fun onEditAuthorDialogPositiveClick(author: Author?) {
        if (author != null) {
            viewModel.editAuthor(author)
        } else {
            Toast.makeText(this, "Don have Author to edit", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onEditAuthorDialogNegativeClick(author: Author?) {
        Toast.makeText(this, "You click cancel", Toast.LENGTH_SHORT).show()
    }

}