package com.ruderarajput.foodieadmin.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.ruderarajput.foodieadmin.Adapter.DeliveryAdapter
import com.ruderarajput.foodieadmin.Models.OrderDetails
import com.ruderarajput.foodieadmin.R
import com.ruderarajput.foodieadmin.databinding.ActivityOutForDeleveryBinding

class OutForDeleveryActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityOutForDeleveryBinding.inflate(layoutInflater)
    }
    private lateinit var database:FirebaseDatabase
    private  var listOfCompleteOrderList:ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        //retrive and display completed order
        retriveCompletedOrderDetails()

    }

    private fun retriveCompletedOrderDetails() {
        database=FirebaseDatabase.getInstance()
        val completedOrderReference=database.reference.child("CompletedOrder").orderByChild("currentTime")
        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                // clear the list before populating in with new Data
                listOfCompleteOrderList.clear()

                for (orderSnapshot in snapshot.children){
                    val completeOrder=orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it)
                    }
                }
                // reverese the list to display latest order first
                listOfCompleteOrderList.reverse()
                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setDataIntoRecyclerView() {
        // Initialization list to hold custumers name and payment status
        val customerName= mutableListOf<String>()
        val moneyStatus= mutableListOf<Boolean>()

        for (order in listOfCompleteOrderList){

            order.userName?.let {
                customerName.add(it)
            }
            moneyStatus.add(order.paymentReceived)
        }
        val adapter=DeliveryAdapter(customerName,moneyStatus)
        binding.recViewOutDelivery.adapter=adapter
        binding.recViewOutDelivery.layoutManager=LinearLayoutManager(this)

    }
}