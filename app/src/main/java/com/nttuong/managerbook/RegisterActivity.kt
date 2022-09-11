package com.nttuong.managerbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.nttuong.managerbook.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.edtUser.text.toString().trim {it <= ' ' }) ->
                {
                    Toast.makeText(
                        this,
                        "Nhập Email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(binding.edtPass.text.toString().trim {it <= ' ' }) ->
                {
                    Toast.makeText(
                        this,
                        "Nhập Mật Khẩu",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email = binding.edtUser.text.toString().trim {it <= ' ' }
                    val pass = binding.edtPass.text.toString().trim {it <= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this){ task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this,
                                    "Đăng Ký thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                }
            }
        }
    }
}