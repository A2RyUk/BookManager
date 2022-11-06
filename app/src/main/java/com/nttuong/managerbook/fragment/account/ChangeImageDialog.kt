package com.nttuong.managerbook.fragment.account

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentAddAuthorDialogBinding
import com.nttuong.managerbook.databinding.FragmentChangeImageDialogBinding
import com.nttuong.managerbook.db.entities.Author

class ChangeImageDialog : DialogFragment() {
    private lateinit var binding: FragmentChangeImageDialogBinding
    private lateinit var listener: ChangeImageDialogListener
    private lateinit var image: Uri

    interface ChangeImageDialogListener {
        fun onChangeImageDialogPositiveClick(img: Uri?)
        fun onChangeImageDialogNegativeClick(img: Uri?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ChangeImageDialogListener
        } catch (e: ClassCastException) {
            throw  ClassCastException((context.toString() + "must implement ChangeImageDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it!!)

            binding = FragmentChangeImageDialogBinding.inflate(layoutInflater)

            binding.btnChange.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_GET_CONTENT
                intent.type ="image/*"
                startActivityForResult(intent, 1)
            }
            builder.setView(binding.root)
                .setPositiveButton(
                    R.string.btn_save
                ) { dialog, id ->
                    listener.onChangeImageDialogPositiveClick(image)
                }
                .setNegativeButton(
                    R.string.btn_cancel
                ) { dialog, id ->
                    listener.onChangeImageDialogNegativeClick(null)
                    getDialog()?.dismiss()
                }
            builder.create()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                image = data.data!!

                binding.imgProfileAva.setImageURI(image)
            }
        }
    }
}