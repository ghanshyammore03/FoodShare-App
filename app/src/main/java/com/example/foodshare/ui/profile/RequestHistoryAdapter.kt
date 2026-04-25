package com.example.foodshare.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodshare.data.model.RequestWithDetails
import com.example.foodshare.databinding.ItemHistoryRequestBinding
import com.example.foodshare.utils.TimeUtils

class RequestHistoryAdapter : RecyclerView.Adapter<RequestHistoryAdapter.ViewHolder>() {

    private val items = mutableListOf<RequestWithDetails>()

    fun submitList(list: List<RequestWithDetails>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHistoryRequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(
        private val binding: ItemHistoryRequestBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RequestWithDetails) {
            binding.tvFoodName.text = item.donation.foodName
            binding.tvMeta.text = item.request.message
            binding.tvTime.text = TimeUtils.formatDateTime(item.request.requestedAt)
            binding.tvStatus.text = item.request.status
        }
    }
}