package com.nttuong.managerbook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.nttuong.managerbook.databinding.ActivityLoginBinding
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val fAuth = FirebaseAuth.getInstance()
    private val fStore = FirebaseFirestore.getInstance()
    private lateinit var viewModel: BookManagerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(BookManagerViewModel::class.java)

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtUser.text.toString().trim {it <= ' ' }
            val pass = binding.edtPass.text.toString().trim {it <= ' ' }
            fAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Đăng Nhập thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                        checkUserAccessLevel(task.result.user!!.uid)
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

    private fun checkUserAccessLevel(uid: String) {
        val df: DocumentReference = fStore.collection("User").document(uid)
        df.get().addOnSuccessListener {
            if (it.getString("IsAdmin") == "isAdmin") {
                val loginIntent = Intent(this, MainActivity::class.java)
                loginIntent.putExtra("AccountName", it.getString("FullName"))
                loginIntent.putExtra("AccountLevel", "Admin")
                startActivity(loginIntent)
                finish()
            } else if (it.getString("IsAdmin") == "isUser") {
                val loginIntent = Intent(this, MainActivity::class.java)
                loginIntent.putExtra("AccountName", it.getString("FullName"))
                loginIntent.putExtra("AccountLevel", "User")
                startActivity(loginIntent)
                finish()
            }
        }
    }
}