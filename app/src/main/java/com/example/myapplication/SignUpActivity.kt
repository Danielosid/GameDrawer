package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            val username = binding.usernameEt.text.toString()
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, SignInActivity::class.java)
                            userID = firebaseAuth.uid.toString()
                            val dataUser = hashMapOf(
                                "username" to username,
                                "email" to email,
                                "id" to userID
                            )
                            db.collection("users").document(userID).set(dataUser)
                            val gameRec = hashMapOf(
                                "value" to false
                            )
                            db.collection("users").document(userID).collection("settings")
                                .document("gameRec")
                                .set(gameRec)
                            val friendReq = hashMapOf(
                                "value" to false
                            )
                            db.collection("users").document(userID).collection("settings")
                                .document("friendReq")
                                .set(friendReq)
                            val interests = hashMapOf(
                                "value" to "everyone"
                            )
                            db.collection("users").document(userID).collection("settings")
                                .document("interests")
                                .set(interests)
                            val game = hashMapOf(
                                "value" to "everyone"
                            )
                            db.collection("users").document(userID).collection("settings")
                                .document("game")
                                .set(game)
                            val friend = hashMapOf(
                                "value" to "everyone"
                            )
                            db.collection("users").document(userID).collection("settings")
                                .document("friend")
                                .set(friend)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Passwords are not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}