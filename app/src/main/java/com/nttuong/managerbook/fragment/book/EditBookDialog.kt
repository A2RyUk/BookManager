package com.nttuong.managerbook.fragment.book

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentAddBookDialogBinding
import com.nttuong.managerbook.databinding.FragmentEditBookDialogBinding
import com.nttuong.managerbook.db.entities.Book

class EditBookDialog : DialogFragment() {
    private lateinit var binding: FragmentEditBookDialogBinding
    private lateinit var listener: EditBookDialogListener

    interface EditBookDialogListener {
        fun onEditBookDialogPositiveClick(book: Book?)
        fun onEditBookDialogNegativeClick(book: Book?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as EditBookDialogListener
        } catch (e: ClassCastException) {
            throw  ClassCastException((context.toString() + "must implement EditBookDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)

            binding = FragmentEditBookDialogBinding.inflate(layoutInflater)

            builder.setView(binding.root)
                .setPositiveButton(R.string.btn_save
                ) { dialog, id ->
                    val id = binding.edtBookId.text.toString()
                    val avatar = binding.edtBookAvatar.text.toString()
                    val name = binding.edtBookName.text.toString()
                    val author = binding.edtBookAuthor.text.toString()
                    val category = binding.edtBookCategory.text.toString()
                    val status = binding.edtBookStatus.text.toString()
                    val content = binding.edtBookContent.text.toString()
                    val book = Book(bookId = id.toInt(), avatar = avatar, name = name, author = author, category = category, status = status, content = content)
                    listener.onEditBookDialogPositiveClick(book)
                }
                .setNegativeButton(R.string.btn_cancel
                ) { dialog, id ->
                    listener.onEditBookDialogNegativeClick(null)
                    getDialog()?.dismiss()
                }

            builder.create()
        }   ?: throw IllegalArgumentException("Activity cannot be null")
    }
}