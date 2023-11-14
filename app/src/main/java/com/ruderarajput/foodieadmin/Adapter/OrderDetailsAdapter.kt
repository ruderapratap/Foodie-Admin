package com.ruderarajput.foodieadmin.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ruderarajput.foodieadmin.databinding.OrderdetailItemBinding

class OrderDetailsAdapter(
    private var context: Context,
    private var foodName: ArrayList<String>,
    private var foodImage: ArrayList<String>,
    private var foodQuantity: ArrayList<Int>,
    private var foodPrice: ArrayList<String>
) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val binding =
            OrderdetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = foodName.size

    inner class OrderDetailsViewHolder(private val binding: OrderdetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.orderItemFoodName.text = foodName[position]
            binding.orderItemQuantity.text = foodQuantity[position].toString()
            val uriString = foodImage[position]
            val uri = Uri.parse(uriString)
            Glide.with(context).load(uri).into(binding.orderItemImage)
            binding.orderItemFoodPrice.text = foodPrice[position]

        }

    }

}
