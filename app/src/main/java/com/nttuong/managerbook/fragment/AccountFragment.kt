package com.nttuong.managerbook.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nttuong.managerbook.activity.LoginActivity
import com.nttuong.managerbook.activity.manager.ManagerAuthorActivity
import com.nttuong.managerbook.activity.manager.ManagerBookActivity
import com.nttuong.managerbook.activity.manager.ManagerCategoryActivity
import com.nttuong.managerbook.databinding.FragmentAccountAdminBinding
import com.nttuong.managerbook.fragment.account.ChangeImageDialog
import java.util.*

class AccountFragment : Fragment(), ChangeImageDialog.ChangeImageDialogListener {

    private lateinit var binding: FragmentAccountAdminBinding
    private val fAuth = FirebaseAuth.getInstance()
    private val fStore = FirebaseFirestore.getInstance()
    private val fStorage = FirebaseStorage.getInstance()
    private lateinit var selectedImg: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountAdminBinding.inflate(layoutInflater)

        val user = fAuth.currentUser!!.uid
        if (user != null) {
            checkUserAccessLevel(user)
        } else {
            binding.constraintManagerBook.visibility = View.GONE
            binding.constraintManagerAuthor.visibility = View.GONE
            binding.constraintManagerCategory.visibility = View.GONE
            binding.txtManagerBook.visibility = View.GONE
            binding.txtAccountName.text = "Tài Khoản"
            binding.txtAccountLevel.text = "Đăng Nhập"
        }

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
        binding.btnLogout.setOnClickListener {
            fAuth.signOut()
            val signOutIntent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(signOutIntent)
        }

        binding.btnChangeAvatar.setOnClickListener {

        }
        
        return binding.root
    }

    private fun checkUserAccessLevel(uid: String) {
        val df: DocumentReference = fStore.collection("User").document(uid)
        df.get().addOnSuccessListener {
            if (it.getString("IsAdmin") == "isAdmin") {
                binding.constraintManagerBook.visibility = View.VISIBLE
                binding.constraintManagerAuthor.visibility = View.VISIBLE
                binding.constraintManagerCategory.visibility = View.VISIBLE
                binding.txtManagerBook.visibility = View.VISIBLE
                binding.txtAccountName.text = it.getString("FullName")
                binding.txtAccountLevel.text = "Quản Trị Viên"
            } else if (it.getString("IsAdmin") == "isUser") {
                binding.constraintManagerBook.visibility = View.GONE
                binding.constraintManagerAuthor.visibility = View.GONE
                binding.constraintManagerCategory.visibility = View.GONE
                binding.txtManagerBook.visibility = View.GONE
                binding.txtAccountName.text = it.getString("FullName")
                binding.txtAccountLevel.text = "Người Dùng"
            }
        }
    }

    override fun onChangeImageDialogPositiveClick(img: Uri?) {
        uploadData(img)
        Toast.makeText(
            requireContext(),
            "Đổi avatar thành công",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onChangeImageDialogNegativeClick(img: Uri?) {
        Toast.makeText(
            requireContext(),
            "You click cancel",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun uploadData(img: Uri?) {
        val reference = fStorage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(img!!).addOnCompleteListener {
            if (it.isSuccessful) {

            }
        }
    }
}