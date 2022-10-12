package com.nttuong.managerbook.fragment.book

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentEditBookDialogBinding
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class EditBookDialog(
    private var oldBook: Book
) : DialogFragment() {
    private lateinit var binding: FragmentEditBookDialogBinding
    private lateinit var listener: EditBookDialogListener
    private val viewModel: BookManagerViewModel by activityViewModels()

    interface EditBookDialogListener {
        fun onEditBookDialogPositiveClick(book: Book?)
        fun onEditBookDialogNegativeClick(book: Book?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as EditBookDialogListener
        } catch (e: ClassCastException) {
            throw  ClassCastException((context.toString() + "must implement EditBookDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it!!)

            binding = FragmentEditBookDialogBinding.inflate(layoutInflater)

            binding.edtBookId.text = oldBook.bookId.toString()
            binding.edtBookAvatar.text = Editable.Factory.getInstance().newEditable(oldBook.avatar)
            binding.edtBookName.text = Editable.Factory.getInstance().newEditable(oldBook.name)
            binding.edtBookAuthor.text = Editable.Factory.getInstance().newEditable(oldBook.author)
            binding.edtBookContent.text = Editable.Factory.getInstance().newEditable(oldBook.content)

            var categoryChoose = ""
            viewModel.getAllCategoryList.observe(this) { list ->
                list.let {
                    val category = viewModel.getAllCategoryToString(it)
                    val categoryArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_statusitem, category)
                    binding.spinnerCategory.adapter = categoryArrayAdapter
                    if (oldBook.category.toString() != null) {
                        val spinnerPosition: Int = categoryArrayAdapter.getPosition(oldBook.category)
                        binding.spinnerCategory.setSelection(spinnerPosition)
                    }
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
            if (oldBook.status.toString() != null) {
                val spinnerPosition: Int = statusArrayAdapter.getPosition(oldBook.status)
                binding.spinnerStatus.setSelection(spinnerPosition)
            }
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
                    val id = binding.edtBookId.text.toString().toInt()
                    val avatar = binding.edtBookAvatar.text.toString()
                    val name = binding.edtBookName.text.toString()
                    val author = binding.edtBookAuthor.text.toString()
                    val category = categoryChoose
                    val statusChoice = statusChoose
                    val content = binding.edtBookContent.text.toString()
                    val book = Book(id, avatar = avatar, name = name, author = author, category = category, status = statusChoice, content = content)
                    listener.onEditBookDialogPositiveClick(book)
                }
                .setNegativeButton(
                    R.string.btn_cancel
                ) { dialog, id ->
                    listener.onEditBookDialogNegativeClick(null)
                    getDialog()?.dismiss()
                }

            builder.create()
        }
    }
}