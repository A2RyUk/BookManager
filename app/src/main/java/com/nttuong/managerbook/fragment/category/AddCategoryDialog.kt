package com.nttuong.managerbook.fragment.category

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentAddCategoryDialogBinding
import com.nttuong.managerbook.db.entities.Category

class AddCategoryDialog : DialogFragment() {
    private lateinit var binding: FragmentAddCategoryDialogBinding
    private lateinit var listener: AddCategoryDialogListener

    interface AddCategoryDialogListener {
        fun onAddCategoryDialogPositiveClick(category: Category?)
        fun onAddCategoryDialogNegativeClick(category: Category?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddCategoryDialogListener
        } catch (e: ClassCastException) {
            throw  ClassCastException((context.toString() + "must implement AddCategoryDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it!!)

            binding = FragmentAddCategoryDialogBinding.inflate(layoutInflater)
            builder.setView(binding.root)
                .setPositiveButton(
                    R.string.btn_save
                ) { dialog, id ->
                    val name = binding.edtCategoryName.text.toString()
                    val category = Category(null, categoryName = name)
                    listener.onAddCategoryDialogPositiveClick(category)
                }
                .setNegativeButton(
                    R.string.btn_cancel
                ) { dialog, id ->
                    listener.onAddCategoryDialogNegativeClick(null)
                    getDialog()?.dismiss()
                }
            builder.create()
        }
    }
}