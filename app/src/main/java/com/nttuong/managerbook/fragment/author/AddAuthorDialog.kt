package com.nttuong.managerbook.fragment.author

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentAddAuthorDialogBinding
import com.nttuong.managerbook.db.entities.Author
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class AddAuthorDialog : DialogFragment() {
    private lateinit var binding: FragmentAddAuthorDialogBinding
    private lateinit var listener: AddAuthorDialogListener
    private val viewModel: BookManagerViewModel by activityViewModels()

    interface AddAuthorDialogListener {
        fun onAddAuthorDialogPositiveClick(author: Author?)
        fun onAddAuthorDialogNegativeClick(author: Author?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddAuthorDialogListener
        } catch (e: ClassCastException) {
            throw  ClassCastException((context.toString() + "must implement AddAuthorDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it!!)

            binding = FragmentAddAuthorDialogBinding.inflate(layoutInflater)

            builder.setView(binding.root)
                .setPositiveButton(
                    R.string.btn_save
                ) { dialog, id ->
                    val avatar = binding.edtAuthorAvatar.text.toString()
                    val name = binding.edtAuthorName.text.toString()
                    val author = Author(null, authorAvatar = avatar, authorName = name)
                    listener.onAddAuthorDialogPositiveClick(author)
                }
                .setNegativeButton(
                    R.string.btn_cancel
                ) { dialog, id ->
                    listener.onAddAuthorDialogNegativeClick(null)
                    getDialog()?.dismiss()
                }

            builder.create()
        }
    }
}