package com.nttuong.managerbook.fragment.author

import android.content.Context
import android.os.Bundle
import android.app.AlertDialog
import android.app.Dialog
import androidx.fragment.app.DialogFragment
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentDeleteAuthorDialogBinding

class DeleteAuthorDialog : DialogFragment() {
    private lateinit var binding: FragmentDeleteAuthorDialogBinding
    private lateinit var listener: DeleteAuthorDialogListener

    interface DeleteAuthorDialogListener {
        fun onDeleteAuthorDialogPositiveClick(authorId: Int?)
        fun onDeleteAuthorDialogNegativeClick(authorId: Int?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as DeleteAuthorDialogListener
        } catch (e: ClassCastException) {
            throw  ClassCastException((context.toString() + "must implement DeleteAuthorDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)

            binding = FragmentDeleteAuthorDialogBinding.inflate(layoutInflater)

            builder.setView(binding.root)
                .setPositiveButton(
                    R.string.btn_delete
                ) { dialog, id ->
                    val id = binding.edtAuthorId.text.toString().toInt()
                    listener.onDeleteAuthorDialogPositiveClick(id)
                }
                .setNegativeButton(
                    R.string.btn_cancel
                ) { dialog, id ->
                    listener.onDeleteAuthorDialogNegativeClick(null)
                    getDialog()?.dismiss()
                }
            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }
}