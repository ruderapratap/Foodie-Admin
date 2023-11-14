package com.ruderarajput.foodieadmin.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ruderarajput.foodieadmin.Models.UserModel
import com.ruderarajput.foodieadmin.databinding.ActivityAdminProfileBinding

class AdminProfileActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReferences: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReferences = database.reference.child("user")
        retriveUserData()


        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.saveInformationBtn.setOnClickListener {
            upDateUserData()
            binding.etProfileName.isEnabled = false
            binding.etResturantName.isEnabled = false
            binding.etProfileAddress.isEnabled = false
            binding.etProfileEmail.isEnabled = false
            binding.etProfileNumber.isEnabled = false
            binding.etProfilePassword.isEnabled = false
            binding.saveInformationBtn.isEnabled = false
        }

        binding.etProfileName.isEnabled = false
        binding.etResturantName.isEnabled = false
        binding.etProfileAddress.isEnabled = false
        binding.etProfileEmail.isEnabled = false
        binding.etProfileNumber.isEnabled = false
        binding.etProfilePassword.isEnabled = false
        binding.saveInformationBtn.isEnabled = false

        binding.editProfile.setOnClickListener {

            binding.etProfileName.isEnabled = !binding.etProfileName.isEnabled
            binding.etResturantName.isEnabled = !binding.etResturantName.isEnabled
            binding.etProfileAddress.isEnabled = !binding.etProfileAddress.isEnabled
            binding.etProfileEmail.isEnabled = !binding.etProfileEmail.isEnabled
            binding.etProfileNumber.isEnabled = !binding.etProfileNumber.isEnabled
            binding.etProfilePassword.isEnabled = !binding.etProfilePassword.isEnabled
            binding.etProfileName.requestFocus()

            binding.saveInformationBtn.isEnabled = !binding.saveInformationBtn.isEnabled
        }
    }

    private fun retriveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = adminReferences.child(currentUserUid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val ownerName = snapshot.child("name").getValue()
                        val nameOfResturent = snapshot.child("nameOfResturent").getValue()
                        val email = snapshot.child("email").getValue()
                        val password = snapshot.child("password").getValue()
                        val address = snapshot.child("address").getValue()
                        val phone = snapshot.child("phone").getValue()
                        setDataToTextView(
                            ownerName,
                            nameOfResturent,
                            email,
                            password,
                            phone,
                            address
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private fun setDataToTextView(
        ownerName: Any?,
        nameOfResturent: Any?,
        email: Any?,
        password: Any?,
        phone: Any?,
        address: Any?
    ) {
        binding.etProfileName.setText(ownerName.toString())
        binding.etResturantName.setText(nameOfResturent.toString())
        binding.etProfileAddress.setText(address.toString())
        binding.etProfileEmail.setText(email.toString())
        binding.etProfileNumber.setText(phone.toString())
        binding.etProfilePassword.setText(password.toString())
    }


    private fun upDateUserData() {
        val updateName = binding.etProfileName.text.toString()
        val updateResturantName = binding.etResturantName.text.toString()
        val updateAddress = binding.etProfileAddress.text.toString()
        val updateEmail = binding.etProfileEmail.text.toString()
        val updatePhoneNumber = binding.etProfileNumber.text.toString()
        val updatePassword = binding.etProfilePassword.text.toString()

        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = adminReferences.child(currentUserUid)
            userReference.child("name").setValue(updateName)
            userReference.child("nameOfResturent").setValue(updateResturantName)
            userReference.child("address").setValue(updateAddress)
            userReference.child("email").setValue(updateEmail)
            userReference.child("phone").setValue(updatePhoneNumber)
            userReference.child("password").setValue(updatePassword)

            Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)

        } else {
            Toast.makeText(this, "Profile Updated Failed", Toast.LENGTH_SHORT).show()
        }

    }
}