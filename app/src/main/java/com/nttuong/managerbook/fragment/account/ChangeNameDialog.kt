package com.nttuong.managerbook.fragment.account

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentChangeNameDialogBinding

class ChangeNameDialog : DialogFragment() {
    private lateinit var binding: FragmentChangeNameDialogBinding
    private lateinit var listener: ChangeNameDialogListener

    interface ChangeNameDialogListener {
        fun onChangeNameDialogPositiveClick(name: String?)
        fun onChangeNameDialogNegativeClick(name: String?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as ChangeNameDialogListener
        } catch (e: ClassCastException) {
            throw  ClassCastException((context.toString() + "must implement ChangeNameDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it!!)

            binding = FragmentChangeNameDialogBinding.inflate(layoutInflater)

            builder.setView(binding.root)
                .setPositiveButton(
                    R.string.btn_save
                ) { dialog, id ->
                    val name = binding.edtChange.text.toString()
                    listener.onChangeNameDialogPositiveClick(name)
                }
                .setNegativeButton(
                    R.string.btn_cancel
                ) { dialog, id ->
                    listener.onChangeNameDialogNegativeClick(null)
                    getDialog()?.dismiss()
                }
            builder.create()

        }
    }
}