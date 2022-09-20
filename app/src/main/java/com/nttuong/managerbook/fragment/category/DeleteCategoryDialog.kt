package com.nttuong.managerbook.fragment.category

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentAddAuthorDialogBinding
import com.nttuong.managerbook.databinding.FragmentAddBookDialogBinding
import com.nttuong.managerbook.databinding.FragmentDeleteCategoryDialogBinding
import com.nttuong.managerbook.db.entities.Author
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.fragment.book.AddBookDialog

class DeleteCategoryDialog : DialogFragment() {

    private lateinit var binding: FragmentDeleteCategoryDialogBinding
    private lateinit var listener: DeleteCategoryDialogBinding

    interface DeleteCategoryDialogBinding {
        fun onDeleteCategoryDialogPositiveClick(categoryId: Int?)
        fun onDeleteCategoryDialogNegativeClick(categoryId: Int?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as DeleteCategoryDialogBinding
        } catch (e: ClassCastException) {
            throw  ClassCastException((context.toString() + "must implement DeleteCategoryDialogBinding"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)

            binding = FragmentDeleteCategoryDialogBinding.inflate(layoutInflater)

            builder.setView(binding.root)
                .setPositiveButton(
                    R.string.btn_save
                ) { dialog, id ->
                    val id = binding.edtCategoryId.text.toString().toInt()
                    listener.onDeleteCategoryDialogPositiveClick(id)
                }
                .setNegativeButton(
                    R.string.btn_cancel
                ) { dialog, id ->
                    listener.onDeleteCategoryDialogNegativeClick(null)
                    getDialog()?.dismiss()
                }

            builder.create()
        }   ?: throw IllegalArgumentException("Activity cannot be null")
    }
}