package com.ruderarajput.foodieadmin.Activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.ruderarajput.foodieadmin.Models.AllMenu
import com.ruderarajput.foodieadmin.R
import com.ruderarajput.foodieadmin.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private var foodImageUri: Uri? = null
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.selectImage.setOnClickListener {
            picImage.launch("image/*")
        }

        binding.addItemBtn.setOnClickListener {
            foodName = binding.etFoodName.text.toString().trim()
            foodPrice = binding.etFoodPrice.text.toString().trim()
            foodDescription = binding.etDescription.text.toString().trim()
            foodIngredient = binding.etIngredient.text.toString().trim()

            if (foodName.isEmpty()) {
                Toast.makeText(this, "Please Add Food Name", Toast.LENGTH_SHORT).show()
                binding.etFoodName.requestFocus()
            } else if (foodPrice.isEmpty()) {
                Toast.makeText(this, "Please Add Food Price", Toast.LENGTH_SHORT).show()
                binding.etFoodPrice.requestFocus()
            } else if (foodDescription.isEmpty()) {
                Toast.makeText(this, "Please Add Food Description", Toast.LENGTH_SHORT).show()
                binding.etDescription.requestFocus()
            } else if (foodIngredient.isEmpty()) {
                Toast.makeText(this, "Please Add Food Ingredient", Toast.LENGTH_SHORT).show()
                binding.etIngredient.requestFocus()
            }else if (foodImageUri==null){
                Toast.makeText(this, "Please Add Food Image", Toast.LENGTH_SHORT).show()
                binding.selectImage.requestFocus()
            } else {
                uploadData()
                Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun uploadData() {
        val menuRef = database.getReference("menu")
        val newItemKey = menuRef.push().key

        if (foodImageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_image/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    val newItem = AllMenu(
                        newItemKey,
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodDescription = foodDescription,
                        foodIngredient = foodIngredient,
                        foodImage = downloadUrl.toString()
                    )
                    newItemKey?.let { key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Food Item Uploaded Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Food Item Uploaded Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Food Image Upload Failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show()
        }
    }

    private val picImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.foodImagePl.setImageURI(uri)
            foodImageUri=uri
        }

    }
}