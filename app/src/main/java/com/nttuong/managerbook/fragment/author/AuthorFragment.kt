package com.nttuong.managerbook.fragment.author

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttuong.managerbook.R
import com.nttuong.managerbook.adapter.AuthorAdapter
import com.nttuong.managerbook.databinding.FragmentAuthorBinding
import com.nttuong.managerbook.db.entities.Author
import com.nttuong.managerbook.viewmodel.AuthorViewModel

class AuthorFragment : Fragment(),
    AddAuthorDialog.AddAuthorDialogListener {
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }
    private var clicked = false

    private lateinit var binding: FragmentAuthorBinding
    private lateinit var adapter: AuthorAdapter
    private val authors = arrayListOf<Author>()
    private lateinit var authorViewModel: AuthorViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthorBinding.inflate(layoutInflater)

        binding.btnMenu.setOnClickListener {
            onShowButtonClicked()
        }
        binding.btnAdd.setOnClickListener {
            val dialog = AddAuthorDialog()
            dialog.show(childFragmentManager, "addAuthorDialog")
        }
        binding.btnEdit.setOnClickListener {

        }
        binding.btnDelete.setOnClickListener {

        }

        authorViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
            .get(AuthorViewModel::class.java)
        authorViewModel.getAllAuthorList.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapter.updateList(it)
            }
        }

        adapter = AuthorAdapter(authors)
        binding.rcAuthor.adapter = adapter
        binding.rcAuthor.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        return binding.root
    }

    private fun onShowButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if(!clicked) {
            binding.btnAdd.visibility = View.VISIBLE
            binding.btnEdit.visibility = View.VISIBLE
            binding.btnDelete.visibility = View.VISIBLE
        } else {
            binding.btnAdd.visibility = View.INVISIBLE
            binding.btnEdit.visibility = View.INVISIBLE
            binding.btnDelete.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked) {
            binding.btnAdd.startAnimation(fromBottom)
            binding.btnEdit.startAnimation(fromBottom)
            binding.btnDelete.startAnimation(fromBottom)
            binding.btnMenu.startAnimation(rotateOpen)
        } else {
            binding.btnAdd.startAnimation(toBottom)
            binding.btnEdit.startAnimation(toBottom)
            binding.btnDelete.startAnimation(toBottom)
            binding.btnMenu.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if(!clicked) {
            binding.btnAdd.isClickable = true
            binding.btnEdit.isClickable = true
            binding.btnDelete.isClickable = true
        } else {
            binding.btnAdd.isClickable = false
            binding.btnEdit.isClickable = false
            binding.btnDelete.isClickable = false
        }
    }

    override fun onAddAuthorDialogPositiveClick(author: Author?) {
        if (author != null) {
            authorViewModel.getAllBookList.observe(viewLifecycleOwner) { list ->
                list?.let { list1 ->
                    var count = 0
                    for (i in list1.indices) {
                        if (author.authorName == list1[i].author) {
                            count++
                        }
                    }
                    author.numberOfBook = count
                    authorViewModel.getAllAuthorList.observe(viewLifecycleOwner) { list ->
                        list?.let {

                        }
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "Don have Author to add", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAddAuthorDialogNegativeClick(author: Author?) {
        Toast.makeText(requireContext(), "You click cancel", Toast.LENGTH_SHORT).show()
    }
}