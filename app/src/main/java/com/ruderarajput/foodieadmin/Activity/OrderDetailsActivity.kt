package com.ruderarajput.foodieadmin.Activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruderarajput.foodieadmin.Adapter.OrderDetailsAdapter
import com.ruderarajput.foodieadmin.Models.OrderDetails
import com.ruderarajput.foodieadmin.databinding.ActivityOrderDetailsBinding

class OrderDetailsActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null
    private var foodNames: ArrayList<String> = arrayListOf()
    private var foodImages: ArrayList<String> = arrayListOf()
    private var foodQuantity: ArrayList<Int> = arrayListOf()
    private var foodPrices: ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        getDataFromIntent()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getDataFromIntent() {

        val receivedOrderDetails = intent.getSerializableExtra("userOrderDetails") as OrderDetails
            receivedOrderDetails?.let {orderDetails ->
                userName = receivedOrderDetails.userName
                foodNames = receivedOrderDetails.foodNames as ArrayList<String>
                foodImages = receivedOrderDetails.foodImages as ArrayList<String>
                foodQuantity = receivedOrderDetails.foodQuantities as ArrayList<Int>
                address = receivedOrderDetails.address
                phoneNumber = receivedOrderDetails.phoneNumber
                foodPrices = receivedOrderDetails.foodPrices as ArrayList<String>
                totalPrice = receivedOrderDetails.totalPrice
                setUserDetails()
                setAdapter()
            }
    }

    private fun setUserDetails() {
        binding.userNameTxt.text=userName
        binding.addressTxt.text=address
        binding.phoneTxt.text=phoneNumber
        binding.foodTotalPrice.text=totalPrice
    }
    private fun setAdapter() {
        binding.recViewOrderDelevery.layoutManager=LinearLayoutManager(this)
        val adapter=OrderDetailsAdapter(this,foodNames,foodImages,foodQuantity,foodPrices)
        binding.recViewOrderDelevery.adapter=adapter
    }
}