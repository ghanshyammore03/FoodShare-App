package com.example.foodshare.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodshare.data.local.entity.DonationEntity
import com.example.foodshare.databinding.ItemHistoryDonationBinding
import com.example.foodshare.utils.TimeUtils

class DonationHistoryAdapter : RecyclerView.Adapter<DonationHistoryAdapter.ViewHolder>() {

    private val items = mutableListOf<DonationEntity>()

    fun submitList(list: List<DonationEntity>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHistoryDonationBinding.inflate(
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
        private val binding: ItemHistoryDonationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DonationEntity) {
            binding.tvFoodName.text = item.foodName
            binding.tvMeta.text = "${item.quantity} • ${item.locationText}"
            binding.tvTime.text = TimeUtils.formatDateTime(item.createdAt)
            binding.tvStatus.text = item.status
        }
    }
}