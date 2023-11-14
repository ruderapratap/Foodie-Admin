package com.ruderarajput.foodieadmin.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ruderarajput.foodieadmin.databinding.PendingOrderitemBinding

class PendingOrderAdapter(
    private val context: Context,
    private val customerName: MutableList<String>,
    private val quantity: MutableList<Int>,
    private val foodImage: MutableList<String>,
    private val itemClicked: OnItemClicked
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

    interface OnItemClicked{
        fun onItemClickListener(position: Int)
        fun onItemSelectedClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingOrderAdapter.PendingOrderViewHolder {
        val binding =
            PendingOrderitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PendingOrderAdapter.PendingOrderViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerName.size

    inner class PendingOrderViewHolder(private val binding: PendingOrderitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.pendingOrederUserName.text = customerName[position]
            binding.quantityPendingOrder.text = quantity[position].toString()
            var uriString = foodImage[position]
            var uri = Uri.parse(uriString)
            Glide.with(context).load(uri).into(binding.itemImage)


            binding.stausOrederCompleteBtn.apply {
                if (!isAccepted) {
                    text = "Accept"
                } else {
                    text = "Dispatch"
                }
                setOnClickListener {
                    if (!isAccepted) {
                        text = "Dispatch"
                        isAccepted = true
                        itemClicked.onItemSelectedClickListener(position)
                    } else {
                        customerName.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                        itemClicked.onItemDispatchClickListener(position)
                    }
                }
            }
            itemView.setOnClickListener {
                itemClicked.onItemClickListener(position)
            }
        }

    }

}