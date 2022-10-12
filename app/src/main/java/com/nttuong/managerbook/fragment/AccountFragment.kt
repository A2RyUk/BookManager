package com.nttuong.managerbook.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nttuong.managerbook.R
import com.nttuong.managerbook.activity.manager.ManagerAuthorActivity
import com.nttuong.managerbook.activity.manager.ManagerBookActivity
import com.nttuong.managerbook.activity.manager.ManagerCategoryActivity
import com.nttuong.managerbook.databinding.FragmentAccountAdminBinding

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountAdminBinding.inflate(layoutInflater)

        binding.btnManagerBook.setOnClickListener {
            val bookIntent = Intent(requireContext(), ManagerBookActivity::class.java)
            startActivity(bookIntent)
        }
        binding.btnManagerAuthor.setOnClickListener {
            val authorIntent = Intent(requireContext(), ManagerAuthorActivity::class.java)
            startActivity(authorIntent)
        }
        binding.btnManagerCategory.setOnClickListener {
            val categoryIntent = Intent(requireContext(), ManagerCategoryActivity::class.java)
            startActivity(categoryIntent)
        }
        
        return binding.root
    }
}