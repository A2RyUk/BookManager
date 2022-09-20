package com.nttuong.managerbook.fragment.author


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentEditAuthorDialogBinding
import com.nttuong.managerbook.db.entities.Author

class EditAuthorDialog : DialogFragment() {
    private lateinit var binding: FragmentEditAuthorDialogBinding
    private lateinit var listener: EditAuthorDialogListener

    interface EditAuthorDialogListener {
        fun onEditAuthorDialogPositiveClick(author: Author?)
        fun onEditAuthorDialogNegativeClick(author: Author?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as EditAuthorDialogListener
        } catch (e: ClassCastException) {
            throw  ClassCastException((context.toString() + "must implement EditAuthorDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)

            binding = FragmentEditAuthorDialogBinding.inflate(layoutInflater)

            builder.setView(binding.root)
                .setPositiveButton(
                    R.string.btn_save
                ) { dialog, id ->
                    val id = binding.edtAuthorId.text.toString().toInt()
                    val avatar = binding.edtAuthorAvatar.text.toString()
                    val name = binding.edtAuthorName.text.toString()
                    val author = Author(authorId = id, authorAvatar = avatar, authorName = name, null)
                    listener.onEditAuthorDialogPositiveClick(author)
                }
                .setNegativeButton(
                    R.string.btn_cancel
                ) { dialog, id ->
                    listener.onEditAuthorDialogNegativeClick(null)
                    getDialog()?.dismiss()
                }

            builder.create()
        }   ?: throw IllegalArgumentException("Activity cannot be null")
    }
}


