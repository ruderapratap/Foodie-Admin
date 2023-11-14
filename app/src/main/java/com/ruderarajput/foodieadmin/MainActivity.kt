package com.ruderarajput.foodieadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ruderarajput.foodieadmin.Activity.AddItemActivity
import com.ruderarajput.foodieadmin.Activity.AdminProfileActivity
import com.ruderarajput.foodieadmin.Activity.AllItemActivity
import com.ruderarajput.foodieadmin.Activity.CreateUserActivity
import com.ruderarajput.foodieadmin.Activity.OutForDeleveryActivity
import com.ruderarajput.foodieadmin.Activity.PendingOrderActivity
import com.ruderarajput.foodieadmin.Activity.SignUpActivity
import com.ruderarajput.foodieadmin.Models.OrderDetails
import com.ruderarajput.foodieadmin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completeOrderRefrence: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()

        binding.addMenuCard.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }
        binding.allMenuCard.setOnClickListener {
            startActivity(Intent(this, AllItemActivity::class.java))
        }
        binding.orederDiaspatch.setOnClickListener {
            startActivity(Intent(this, OutForDeleveryActivity::class.java))
        }
        binding.profile.setOnClickListener {
            startActivity(Intent(this, AdminProfileActivity::class.java))
        }
        binding.createNewUser.setOnClickListener {
            startActivity(Intent(this, CreateUserActivity::class.java))
        }
        binding.pendingOrder.setOnClickListener {
            startActivity(Intent(this, PendingOrderActivity::class.java))
        }
        binding.logoutBtn.setOnClickListener {
            val builder=AlertDialog.Builder(this)
            builder.setTitle("LogOut")
            builder.setMessage("Do you want to LogOut")
            builder.setIcon(R.drawable.logout_ic)
            builder.setPositiveButton("Yes"){dialogInterface,which->
                auth.signOut()
                startActivity(Intent(this@MainActivity,SignUpActivity::class.java))
                finish()
            }
            builder.setNegativeButton("No"){dialogInterface,which->
            }
            val dialog=builder.create()
            dialog.setCancelable(false)
            dialog.show()
            }
        pendingOrders()
        completedOrder()
        wholeTimeEarning()
    }

    private fun wholeTimeEarning() {
        var listOfTotalPay = mutableListOf<Int>()
        completeOrderRefrence = FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completeOrderRefrence.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    var completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.totalPrice?.replace("", "")?.toIntOrNull()?.let { i ->
                        listOfTotalPay.add(i)
                    }
                }
                val allEarning = listOfTotalPay.sum().toString()

                if (allEarning.toLong() in 1000..9999) {
                    binding.allEarning.text = "₹ ${allEarning[0]}.${allEarning[1]}K"
                } else if (allEarning.toLong() in 10000..99999) {
                    binding.allEarning.text = "₹ ${allEarning[0]}${allEarning[1]}.${allEarning[2]}K"
                } else if (allEarning.toLong() in 100000..999999) {
                    binding.allEarning.text = "₹ ${allEarning[0]}.${allEarning[1]}${allEarning[2]}L"
                } else if (allEarning.toLong() in 1000000..9999999) {
                    binding.allEarning.text = "₹ ${allEarning[0]}${allEarning[1]}.${allEarning[2]}L"
                } else if (allEarning.toLong() in 10000000..99999999) {
                    binding.allEarning.text = "₹ ${allEarning[0]}.${allEarning[1]}${allEarning[2]}C"
                } else if (allEarning.toLong() in 100000000..999999999) {
                    binding.allEarning.text = "₹ ${allEarning[0]}${allEarning[1]}.${allEarning[2]}C"
                } else {
                    binding.allEarning.text = "₹ $allEarning"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun completedOrder() {
        val completedOrderReference = database.reference.child("CompletedOrder")
        var completedOrderItemCount = 0
        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                completedOrderItemCount = snapshot.childrenCount.toInt()
                binding.completedTxt.text = completedOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun pendingOrders() {
        database = FirebaseDatabase.getInstance()
        val pendingOrderReference = database.reference.child("OrderDetails")
        var pendingOrderItemCount = 0
        pendingOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount = snapshot.childrenCount.toInt()
                binding.pendingTxt.text = pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}