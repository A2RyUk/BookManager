package com.nttuong.managerbook.fragment.author

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentEditAuthorDialogBinding
import com.nttuong.managerbook.db.entities.Author

class EditAuthorDialog(
    private var oldAuthor: Author
) : DialogFragment() {
    private lateinit var binding: FragmentEditAuthorDialogBinding
    private lateinit var listener: EditAuthorDialogListener

    interface EditAuthorDialogListener {
        fun onEditAuthorDialogPositiveClick(author: Author?)
        fun onEditAuthorDialogNegativeClick(author: Author?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as EditAuthorDialogListener
        } catch (e: ClassCastException) {
            throw  ClassCastException((context.toString() + "must implement EditAuthorDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it!!)

            binding = FragmentEditAuthorDialogBinding.inflate(layoutInflater)

            binding.edtAuthorId.text = oldAuthor.authorId.toString()
            binding.edtAuthorAvatar.text = Editable.Factory.getInstance().newEditable(oldAuthor.authorAvatar)
            binding.edtAuthorName.text = Editable.Factory.getInstance().newEditable(oldAuthor.authorName)

            builder.setView(binding.root)
                .setPositiveButton(
                    R.string.btn_save
                ) { dialog, id ->
                    val id = binding.edtAuthorId.text.toString().toInt()
                    val avatar = binding.edtAuthorAvatar.text.toString()
                    val name = binding.edtAuthorName.text.toString()
                    val author = Author(authorId = id, authorAvatar = avatar, authorName = name)
                    listener.onEditAuthorDialogPositiveClick(author)
                }
                .setNegativeButton(
                    R.string.btn_cancel
                ) { dialog, id ->
                    listener.onEditAuthorDialogNegativeClick(null)
                    getDialog()?.dismiss()
                }
            builder.create()
        }
    }
}