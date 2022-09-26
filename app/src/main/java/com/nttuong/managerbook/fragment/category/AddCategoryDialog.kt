package com.nttuong.managerbook.fragment.category

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentAddCategoryDialogBinding
import com.nttuong.managerbook.db.entities.Category

class AddCategoryDialog : DialogFragment() {

    private lateinit var binding: FragmentAddCategoryDialogBinding
    private lateinit var listener: AddCategoryDialogBinding

    interface AddCategoryDialogBinding{
        fun onAddCategoryDialogPositiveClick(category: Category?)
        fun onAddCategoryDialogNegativeClick(category: Category?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as AddCategoryDialogBinding
        } catch (e: ClassCastException) {
            throw  ClassCastException((context.toString() + "must implement AddCategoryDialogBinding"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)

            binding = FragmentAddCategoryDialogBinding.inflate(layoutInflater)

            builder.setView(binding.root)
                .setPositiveButton(
                    R.string.btn_save
                ) { dialog, id ->
                    val avatar = binding.edtCategoryAvatar.text.toString()
                    val name = binding.edtCategoryName.text.toString()
                    val category = Category(categoryId = null, categoryAvatar = avatar, categoryName = name, null)
                    listener.onAddCategoryDialogPositiveClick(category)
                }
                .setNegativeButton(
                    R.string.btn_cancel
                ) { dialog, id ->
                    listener.onAddCategoryDialogNegativeClick(null)
                    getDialog()?.dismiss()
                }

            builder.create()
        }   ?: throw IllegalArgumentException("Activity cannot be null")
    }
}