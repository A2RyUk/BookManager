package com.nttuong.managerbook.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nttuong.managerbook.R
import com.nttuong.managerbook.activity.ChangePassActivity
import com.nttuong.managerbook.activity.LoginActivity
import com.nttuong.managerbook.activity.manager.ManagerAuthorActivity
import com.nttuong.managerbook.activity.manager.ManagerBookActivity
import com.nttuong.managerbook.activity.manager.ManagerCategoryActivity
import com.nttuong.managerbook.databinding.FragmentAccountAdminBinding
import com.nttuong.managerbook.fragment.account.ChangeImageDialog
import com.nttuong.managerbook.fragment.account.ChangeNameDialog
import java.util.*

class AccountFragment : Fragment(), 
    ChangeImageDialog.ChangeImageDialogListener,
    ChangeNameDialog.ChangeNameDialogListener {

    private lateinit var binding: FragmentAccountAdminBinding
    private val fAuth = FirebaseAuth.getInstance()
    private val fStore = FirebaseFirestore.getInstance()
    private val fStorage = FirebaseStorage.getInstance()
    private val userUid = fAuth.currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountAdminBinding.inflate(layoutInflater)

        if (userUid != null) {
            checkUserAccessLevel(userUid)
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
            val avatarDialog = ChangeImageDialog()
            avatarDialog.show(childFragmentManager, "changeAvatarDialog")
        }

        binding.btnChangeName.setOnClickListener {
            val nameDialog = ChangeNameDialog()
            nameDialog.show(childFragmentManager, "changeNameDialog")
        }
        binding.btnChangePass.setOnClickListener {
            val intent = Intent(requireContext(), ChangePassActivity::class.java)
            startActivity(intent)
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
                updateImage(it.getString("imgURL").toString())
            } else if (it.getString("IsAdmin") == "isUser") {
                binding.constraintManagerBook.visibility = View.GONE
                binding.constraintManagerAuthor.visibility = View.GONE
                binding.constraintManagerCategory.visibility = View.GONE
                binding.txtManagerBook.visibility = View.GONE
                binding.txtAccountName.text = it.getString("FullName")
                binding.txtAccountLevel.text = "Người Dùng"
                updateImage(it.getString("imgURL").toString())
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
        binding.imgAva.setImageURI(img)
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
                reference.downloadUrl.addOnSuccessListener { task ->
                    updateAvatarInfo(task.toString())
                }
            }
        }
    }

    private fun updateAvatarInfo(imgUrl: String) {
        var name: String
        var email: String
        var pass: String
        var admin: String
        val df: DocumentReference = fStore.collection("User").document(userUid)
        val userInfo = HashMap<String, Any>()
        df.get().addOnSuccessListener {
            name = it.getString("FullName").toString()
            email = it.getString("Email").toString()
            pass = it.getString("PassWord").toString()
            admin = it.getString("IsAdmin").toString()
            userInfo["FullName"] = name
            userInfo["Email"] = email
            userInfo["PassWord"] = pass
            userInfo["IsAdmin"] = admin
            userInfo["imgURL"] = imgUrl
            df.set(userInfo)
        }
    }

    private fun updateImage(imgUrl: String) {
        Glide.with(binding.imgAva)
            .load(imgUrl)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(binding.imgAva)
    }

    override fun onChangeNameDialogPositiveClick(name: String?) {
        if (name != null) {
            binding.txtAccountName.text = name
            changeName(name)
        }
        Toast.makeText(
            requireContext(),
            "Đổi tên thành công",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onChangeNameDialogNegativeClick(name: String?) {
        Toast.makeText(
            requireContext(),
            "You click cancel",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun changeName(name: String) {
        val df: DocumentReference = fStore.collection("User").document(userUid)
        val userInfo = HashMap<String, Any>()
        var email: String
        var pass: String
        var admin: String
        var imgUrl: String
        df.get().addOnSuccessListener {
            imgUrl = it.getString("imgURL").toString()
            email = it.getString("Email").toString()
            pass = it.getString("PassWord").toString()
            admin = it.getString("IsAdmin").toString()
            userInfo["FullName"] = name
            userInfo["Email"] = email
            userInfo["PassWord"] = pass
            userInfo["IsAdmin"] = admin
            userInfo["imgURL"] = imgUrl
            df.set(userInfo)
        }
    }
}