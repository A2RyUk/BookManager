package com.nttuong.managerbook.fragment.chapter

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentAddChapterDialogBinding
import com.nttuong.managerbook.db.entities.Chapter

class AddChapterDialog : DialogFragment() {
    private lateinit var binding: FragmentAddChapterDialogBinding
    internal lateinit var listener: AddChapterDialogListener

    interface AddChapterDialogListener {
        fun onAddChapterPositiveClick(chapter: Chapter?)
        fun onAddChapterNegativeClick(chapter: Chapter?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as AddChapterDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement AddChapterDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            binding = FragmentAddChapterDialogBinding.inflate(layoutInflater)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(binding.root)
                // Add action buttons
                .setPositiveButton(R.string.btn_save
                ) { dialog, id ->
                    val chapName = binding.edtChapName.text.toString()
                    val chapNumber = binding.edtChapNumber.text.toString()
                    val chapContent = binding.edtChapContent.text.toString()
                    val chapter = Chapter(chapterId = null, chapName = chapName, chapNumber = chapNumber.toInt(), content = chapContent, bookName = null)
                    listener.onAddChapterPositiveClick(chapter)
                }
                .setNegativeButton(R.string.btn_cancel
                ) { dialog, id ->
                    listener.onAddChapterNegativeClick(null)
                    getDialog()?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}