package com.ruderarajput.foodieadmin.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.ruderarajput.foodieadmin.Models.AllMenu
import com.ruderarajput.foodieadmin.databinding.SampleAllitemBinding

class AllItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClicklistner: (position: Int) -> Unit
) : RecyclerView.Adapter<AllItemAdapter.AllItemViewHolder>() {

    private val itemQuantites = IntArray(menuList.size) { 1 }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllItemAdapter.AllItemViewHolder {
        val binding =
            SampleAllitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllItemAdapter.AllItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size

    inner class AllItemViewHolder(private val binding: SampleAllitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantites[position]
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)
                itemDisName.text = menuItem.foodName
                itemCartRupees.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(itemImage)
                cartItemQuantity.text = quantity.toString()
                plusCart.setOnClickListener {
                    increaseQuantites(position)
                }
                minusCart.setOnClickListener {
                    decreaseQuantites(position)
                }
                deleteCart.setOnClickListener {
                    onDeleteClicklistner(position)
                }
            }
        }

        private fun deleteQuantites(position: Int) {
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)
        }

        private fun decreaseQuantites(position: Int) {
            if (itemQuantites[position] > 1) {
                itemQuantites[position]--
                binding.cartItemQuantity.text = itemQuantites[position].toString()
            }
        }

        private fun increaseQuantites(position: Int) {
            if (itemQuantites[position] < 10) {
                itemQuantites[position]++
                binding.cartItemQuantity.text = itemQuantites[position].toString()
            }
        }

    }

}