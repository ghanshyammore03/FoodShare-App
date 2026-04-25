package com.example.foodshare.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodshare.R
import com.example.foodshare.data.model.DonationWithDonor
import com.example.foodshare.databinding.ItemDonationBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DonationAdapter(
    private val onItemClick: (DonationWithDonor) -> Unit
) : RecyclerView.Adapter<DonationAdapter.DonationViewHolder>() {

    private val items = mutableListOf<DonationWithDonor>()

    fun submitList(list: List<DonationWithDonor>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val binding = ItemDonationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DonationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class DonationViewHolder(
        private val binding: ItemDonationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DonationWithDonor) {
            binding.tvFoodName.text = item.donation.foodName
            binding.tvQuantity.text = "Quantity: ${item.donation.quantity}"
            binding.tvLocation.text = "Location: ${item.donation.locationText}"
            binding.tvTime.text = "Pickup before ${formatTime(item.donation.expiryTime)}"
            binding.tvDonorName.text = "Donor: ${item.donor.fullName}"

            if (!item.donation.imagePath.isNullOrBlank()) {
                binding.ivFood.load(item.donation.imagePath) {
                    placeholder(R.drawable.ic_food_placeholder)
                    error(R.drawable.ic_food_placeholder)
                }
            } else {
                binding.ivFood.setImageResource(R.drawable.ic_food_placeholder)
            }

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }

        private fun formatTime(timeMillis: Long): String {
            val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
            return formatter.format(Date(timeMillis))
        }
    }
}