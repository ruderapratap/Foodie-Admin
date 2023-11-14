package com.ruderarajput.foodieadmin.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ruderarajput.foodieadmin.R
import com.ruderarajput.foodieadmin.databinding.ActivityCreateUserBinding

class CreateUserActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityCreateUserBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}