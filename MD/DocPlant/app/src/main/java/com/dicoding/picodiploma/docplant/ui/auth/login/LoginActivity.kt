package com.dicoding.picodiploma.docplant.ui.auth.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.dicoding.picodiploma.docplant.R
import com.dicoding.picodiploma.docplant.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.docplant.ui.auth.register.RegisterActivity
import com.dicoding.picodiploma.docplant.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class  LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {

        binding.signup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener{
            val db = FirebaseFirestore.getInstance()

            val user: MutableMap<String, Any> = HashMap()
            user["email"] = "ayam@email.com"
            user["nama"] = "akuganteng"
            user["password"] = "ikankerapu"

            // Add a new document with a generated ID
            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(
                        this, "You are sukses", Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this, "You are gak sukses", Toast.LENGTH_SHORT
                    ).show()
                }
            val email: String = binding.etEmail.text.toString().trim { it <= ' '}
            val password: String = binding.etPassword.text.toString().trim() { it <= ' '}

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!

//                        Toast.makeText(
//                            this, "You are logged in successfully", Toast.LENGTH_SHORT
//                        ).show()

                        /*db.collection("users")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    Log.d("200", "${document.id} => ${document.data}")
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w("404", "Error getting documents.", exception)
                            }

                         */
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                        intent.putExtra("email_id", email)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this, task.exception!!.message.toString(), Toast.LENGTH_SHORT
                        ).show()
                    }

                }
        }
    }

}