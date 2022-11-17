package com.nttuong.managerbook.fragment.bookcase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.FragmentWasDownloadBinding

class WasDownloadFragment : Fragment() {

    private lateinit var binding: FragmentWasDownloadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWasDownloadBinding.inflate(inflater, container, false)
        return binding.root
    }
}