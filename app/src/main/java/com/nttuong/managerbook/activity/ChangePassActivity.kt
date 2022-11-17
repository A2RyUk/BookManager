package com.nttuong.managerbook.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.nttuong.managerbook.databinding.ActivityChangePassBinding

class ChangePassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePassBinding
    private val fAuth = FirebaseAuth.getInstance()
    private val fStore = FirebaseFirestore.getInstance()
    private val userUid = fAuth.currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.btnChangePass.setOnClickListener {
            changePassWord()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changePassWord() {
        if (binding.edtOldPass.text.isNotEmpty() && binding.edtNewPass.text.isNotEmpty() && binding.edtConfirmPass.text.isNotEmpty()) {
            if (binding.edtNewPass.text.toString() == binding.edtConfirmPass.text.toString()) {
                val user = fAuth.currentUser
                if (user != null && user.email != null) {
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, binding.edtOldPass.text.toString())
                    user.reauthenticate(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val newPassword = binding.edtNewPass.text.toString()
                                uploadNewPass(newPassword)
                                user!!.updatePassword(binding.edtNewPass.text.toString())
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this, "Đổi Pass Thành Công! Hãy đăng nhập.", Toast.LENGTH_LONG).show()
                                            fAuth.signOut()
                                            startActivity(Intent(this, LoginActivity::class.java))
                                            finish()
                                        }
                                    }
                            } else {
                                binding.tvNotifications.text = "Nhập Sai Mật Khẩu Hiện Tại"
                                binding.tvNotifications.setTextColor(Color.parseColor("#B80D00"))
                            }
                        }
                } else {
                    Toast.makeText(this, "Bạn chưa đăng nhập! Hãy đăng nhập.", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            } else {
                binding.tvNotifications.text = "Mật Khẩu Mới Không Khớp"
                binding.tvNotifications.setTextColor(Color.parseColor("#B80D00"))
            }
        } else {
            binding.tvNotifications.text = "Hãy Nhập Đủ Hết Thông Tin"
            binding.tvNotifications.setTextColor(Color.parseColor("#B80D00"))
        }
    }

    private fun uploadNewPass(newPass: String) {
        Log.d("PassWord", "uploadNewPass: $newPass")
        val df: DocumentReference = fStore.collection("User").document(userUid)
        val pass = hashMapOf("PassWord" to newPass)
        df.set(pass, SetOptions.merge())
    }
}