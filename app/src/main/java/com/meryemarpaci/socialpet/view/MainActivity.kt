package com.meryemarpaci.socialpet.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
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
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        val currentUser = auth.currentUser

        currentUser?.let { goToTheFeedActivity() }

        with(binding) {
            buttonCreateAccount.setOnClickListener {
                email = editTextEmail.text.toString()
                password = editTextPassword.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@MainActivity) { task ->
                        if (task.isSuccessful) {
                            val username = editTextUsername.text.toString()

                            val profile = UserProfileChangeRequest.Builder()
                                .setDisplayName(username.ifEmpty { email })
                                .build()

                            auth.currentUser?.updateProfile(profile)?.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    goToTheFeedActivity()
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Could not register username",
                                        Toast.LENGTH_SHORT,
                                    ).show()

                                    goToTheFeedActivity()
                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                this@MainActivity,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
                    .addOnFailureListener {
                        println("createUserWithEmail:failure ${it.localizedMessage}")
                    }
            }

            buttonLogin.setOnClickListener {
                email = editTextEmail.text.toString()
                password = editTextPassword.text.toString()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@MainActivity) { task ->
                        if (task.isSuccessful) {
                            goToTheFeedActivity()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                this@MainActivity,
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

    private fun goToTheFeedActivity() {
        val intent = Intent(this@MainActivity, FeedActivity::class.java)
        startActivity(intent)
        finish()
    }
}