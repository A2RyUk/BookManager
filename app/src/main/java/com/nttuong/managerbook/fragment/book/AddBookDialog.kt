package com.nttuong.managerbook.fragment.book

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentAddBookDialogBinding
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.viewmodel.BookManagerViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class AddBookDialog : DialogFragment() {
    private lateinit var binding: FragmentAddBookDialogBinding
    private lateinit var listener: AddBookDialogListener
    private val viewModel: BookManagerViewModel by activityViewModels()

    interface AddBookDialogListener {
        fun onAddBookDialogPositiveClick(book: Book?)
        fun onAddBookDialogNegativeClick(book: Book?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddBookDialogListener
        } catch (e: ClassCastException) {
            throw  ClassCastException((context.toString() + "must implement AddBookDialogListener"))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it!!)

            binding = FragmentAddBookDialogBinding.inflate(layoutInflater)

            var categoryChoose = ""
            viewModel.getAllCategoryList.observe(this) { list ->
                list.let {
                    val category = viewModel.getAllCategoryToString(it)
                    val categoryArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_statusitem, category)
                    binding.spinnerCategory.adapter = categoryArrayAdapter
                    binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            categoryChoose = category[position]
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }
                    }
                }
            }

            var statusChoose = ""
            val status = resources.getStringArray(R.array.status)
            val statusArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_statusitem, status)
            binding.spinnerStatus.adapter = statusArrayAdapter
            binding.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    statusChoose = status[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

            builder.setView(binding.root)
                .setPositiveButton(
                    R.string.btn_save
                ) { dialog, id ->
                    val avatar = binding.edtBookAvatar.text.toString()
                    val name = binding.edtBookName.text.toString()
                    val author = binding.edtBookAuthor.text.toString()
                    val category = categoryChoose
                    val statusChoice = statusChoose
                    val content = binding.edtBookContent.text.toString()
                    val postTime = LocalDateTime.now()
                    val updateTime = LocalDateTime.now()
                    val book = Book(null, avatar = avatar, name = name, author = author, category = category, status = statusChoice, content = content, postTime = postTime, updateTime = updateTime)
                    listener.onAddBookDialogPositiveClick(book)
                }
                .setNegativeButton(
                    R.string.btn_cancel
                ) { dialog, id ->
                    listener.onAddBookDialogNegativeClick(null)
                    getDialog()?.dismiss()
                }

            builder.create()
        }
    }
}


