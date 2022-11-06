package com.nttuong.managerbook.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.nttuong.managerbook.databinding.ActivityRegisterBinding
import java.util.Objects

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fAuth = FirebaseAuth.getInstance()
        val fStore = FirebaseFirestore.getInstance()

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
                    val name = binding.edtName.text.toString().trim {it <= ' ' }

                    fAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this){ task ->
                            if (task.isSuccessful) {
                                val user = fAuth.currentUser
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this,
                                    "Đăng Ký thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val df = fStore.collection("User").document(user!!.uid)
                                val userInfo = HashMap<String, Any>()
                                userInfo["FullName"] = name
                                userInfo["Email"] = email
                                userInfo["PassWord"] = pass
                                userInfo["IsAdmin"] = "isUser"
                                df.set(userInfo)
                                val intentFinish = Intent(this, LoginActivity::class.java)
                                startActivity(intentFinish)
                                finish()
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