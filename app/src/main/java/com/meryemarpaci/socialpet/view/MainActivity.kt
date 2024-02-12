package com.meryemarpaci.socialpet.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.meryemarpaci.socialpet.databinding.ActivityMainBinding

//firebase json dosyası app klasörü altına
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var email: String = ""
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // Initialize Firebase Auth
        auth = Firebase.auth
        val currentUSer = auth.currentUser
        if (currentUSer != null) {
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.button3.setOnClickListener {
            email = binding.username.text.toString()
            password = binding.password.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    println("create user2")
                    Log.d(TAG, "create")
                    if (task.isSuccessful) {
                        val intent = Intent(this, FeedActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        println("createUserWithEmail:failure ${task.exception}")
                        Toast.makeText(
                            this,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
                .addOnFailureListener {
                    println("createUserWithEmail:failure ${it.localizedMessage}")
                }
        }

        binding.buttonLogin.setOnClickListener {
            email = binding.username.text.toString()
            password = binding.password.text.toString()
            println("login")
            println("login: auth = ${auth.currentUser}")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    println("sign in")
                    if (task.isSuccessful) {
                        val intent = Intent(this, FeedActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        println("signInWithEmail:failure ${task.exception}")
                        Toast.makeText(
                            this,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
                .addOnFailureListener {
                    println("createUserWithEmail:failure ${it.localizedMessage}")
                }
        }
    }
}