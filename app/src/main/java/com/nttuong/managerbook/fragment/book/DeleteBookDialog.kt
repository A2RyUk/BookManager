package com.nttuong.managerbook.fragment.book

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentAddBookDialogBinding
import com.nttuong.managerbook.databinding.FragmentDeleteBookDialogBinding
import com.nttuong.managerbook.db.entities.Book

class DeleteBookDialog : DialogFragment() {
    private lateinit var binding: FragmentDeleteBookDialogBinding
    private lateinit var listener: DeleteBookDialogListener

    interface DeleteBookDialogListener {
        fun onDeleteBookDialogPositiveClick(bookId: Int?)
        fun onDeleteBookDialogNegativeClick(bookId: Int?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as DeleteBookDialogListener
        } catch (e: ClassCastException) {
            throw  ClassCastException((context.toString() + "must implement AddBookDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)

            binding = FragmentDeleteBookDialogBinding.inflate(layoutInflater)

            builder.setView(binding.root)
                .setPositiveButton(
                    R.string.btn_save
                ) { dialog, id ->
                    val id = binding.edtBookId.text.toString()
                    listener.onDeleteBookDialogPositiveClick(id.toInt())
                }
                .setNegativeButton(
                    R.string.btn_cancel
                ) { dialog, id ->
                    listener.onDeleteBookDialogNegativeClick(null)
                    getDialog()?.dismiss()
                }

            builder.create()
        }   ?: throw IllegalArgumentException("Activity cannot be null")
    }
}