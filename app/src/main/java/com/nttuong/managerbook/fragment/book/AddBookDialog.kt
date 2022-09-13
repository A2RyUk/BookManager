package com.nttuong.managerbook.fragment.book

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentAddBookDialogBinding
import com.nttuong.managerbook.db.entities.Book


class AddBookDialog : DialogFragment() {

    private lateinit var binding: FragmentAddBookDialogBinding
    private lateinit var listener: AddBookDialogListener

    interface AddBookDialogListener {
        fun onAddBookDialogPositiveClick(book: Book?)
        fun onAddBookDialogNegativeClick(book: Book?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as AddBookDialogListener
        } catch (e: ClassCastException) {
            throw  ClassCastException((context.toString() + "must implement AddBookDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)

            binding = FragmentAddBookDialogBinding.inflate(layoutInflater)

            var statusChoose = ""
            val status = resources.getStringArray(R.array.category)
            val statusArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_statusitem, status)
            binding.spinnerStatus.adapter = statusArrayAdapter
            binding.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    statusChoose = status[position]
                    Log.d(TAG, "statusChoose: ${status[position]}")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

            builder.setView(binding.root)
                .setPositiveButton(R.string.btn_save
                ) { dialog, id ->
                    val avatar = binding.edtBookAvatar.text.toString()
                    val name = binding.edtBookName.text.toString()
                    val author = binding.edtBookAuthor.text.toString()
                    val category = binding.edtBookCategory.text.toString()
                    val status = statusChoose
                    val content = binding.edtBookContent.text.toString()
                    val book = Book(null, avatar = avatar, name = name, author = author, category = category, status = status, content = content)
                    listener.onAddBookDialogPositiveClick(book)
                }
                .setNegativeButton(R.string.btn_cancel
                ) { dialog, id ->
                    listener.onAddBookDialogNegativeClick(null)
                    getDialog()?.dismiss()
                }

            builder.create()
        }   ?: throw IllegalArgumentException("Activity cannot be null")
    }
}