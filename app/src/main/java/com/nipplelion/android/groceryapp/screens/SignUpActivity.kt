package com.nipplelion.android.groceryapp.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nipplelion.android.groceryapp.R

private const val TAG: String = "SignUpActivity"

class SignUpActivity : AppCompatActivity() {

    private lateinit var btnSignup: Button
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var auth: FirebaseAuth

    private val database = Firebase.database
    private val homesRef = database.getReference("homes/home1/users")
    private val profilesRef = database.getReference("profiles/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth

        btnSignup = findViewById(R.id.btnSignUp)
        etEmail = findViewById(R.id.etEmail)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)

        val extras: Bundle? = intent.extras
        if (extras != null) {
            etEmail.setText(extras.getString("email"))
            etPassword.setText(extras.getString("password"))
        }

        btnSignup.setOnClickListener {
            if (!etEmail.text.toString().isNullOrEmpty() && !etPassword.text.toString().isNullOrEmpty() && !etUsername.text.toString().isNullOrEmpty()) {
                auth.createUserWithEmailAndPassword(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            homesRef.child(user?.uid.toString())

                            var newProfileRef = profilesRef.child(user?.uid.toString())
                            newProfileRef.child("username").setValue(etUsername.text)
                            newProfileRef.child("accountCreated").setValue(System.currentTimeMillis())
                            newProfileRef.child("profilePicture").setValue("")
                            newProfileRef.child("email").setValue(etEmail.text)
                            updateUserUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(baseContext, "Invalid username or email.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUserUI(user: FirebaseUser?) {
        if(user != null){
            // TODO: pass user data into main screen
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}