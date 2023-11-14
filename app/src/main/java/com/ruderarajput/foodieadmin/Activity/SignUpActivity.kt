package com.ruderarajput.foodieadmin.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ruderarajput.foodieadmin.MainActivity
import com.ruderarajput.foodieadmin.Models.UserModel
import com.ruderarajput.foodieadmin.R
import com.ruderarajput.foodieadmin.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var nameOfResturent: String
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.database.reference

        val locationList = arrayOf("Chandigarh", "Mohali", "Bijnor", "Noida", "Noorpur")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

        binding.alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.signUpBtn.setOnClickListener {
            email = binding.etEmail.text.toString().trim()
            userName = binding.etUserName.text.toString().trim()
            password = binding.etPassword.text.toString().trim()
            nameOfResturent = binding.etResturentName.text.toString().trim()
            if (!userName.isNotEmpty()) {
                Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show()
                binding.etUserName.requestFocus()
            } else if (!nameOfResturent.isNotEmpty()) {
                Toast.makeText(this, "Please Enter Your Resturent Name", Toast.LENGTH_SHORT).show()
                binding.etResturentName.requestFocus()
            } else if (!email.isNotEmpty()) {
                Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show()
                binding.etEmail.requestFocus()
            } else if (!password.isNotEmpty()) {
                Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show()
                binding.etPassword.requestFocus()
            } else {
                createAccount(email, password)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account Created Succesfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Account Created Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData() {
        email = binding.etEmail.text.toString().trim()
        userName = binding.etUserName.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        nameOfResturent = binding.etResturentName.text.toString().trim()

        val user=UserModel(userName,nameOfResturent,email,password)
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }
}