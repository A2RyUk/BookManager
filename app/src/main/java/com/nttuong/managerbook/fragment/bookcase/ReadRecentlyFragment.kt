package com.nttuong.managerbook.fragment.bookcase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentReadRecentlyBinding
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class ReadRecentlyFragment : Fragment() {

    private lateinit var binding: FragmentReadRecentlyBinding
    private val viewModel: BookManagerViewModel by activityViewModels()
    private val fAuth = FirebaseAuth.getInstance()
    private val fStore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadRecentlyBinding.inflate(inflater, container, false)
        return binding.root
    }

}