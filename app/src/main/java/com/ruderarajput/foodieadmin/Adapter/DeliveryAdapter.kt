package com.ruderarajput.foodieadmin.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ruderarajput.foodieadmin.databinding.DeliveryItemBinding

class DeliveryAdapter(
    private val customersNames: MutableList<String>,
    private val statusMoney: MutableList<Boolean>
) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeliveryAdapter.DeliveryViewHolder {
        val binding =
            DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryAdapter.DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customersNames.size

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.customerName.text = customersNames[position]
            if (statusMoney[position]==true){
                binding.moneyStatus.text="Received"
            }else{
                binding.moneyStatus.text="Not Recevied"
            }

            val colorMap = mapOf(
                true to Color.GREEN, false to Color.RED
            )
            binding.moneyStatus.setTextColor(colorMap[statusMoney[position]] ?: Color.BLACK)
            binding.cardStatus.backgroundTintList =
                ColorStateList.valueOf(colorMap[statusMoney[position]] ?: Color.BLACK)
        }

    }

}