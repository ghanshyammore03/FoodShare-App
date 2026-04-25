package com.example.foodshare.ui.requests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodshare.data.model.RequestWithDetails
import com.example.foodshare.databinding.ItemManageRequestBinding
import com.example.foodshare.utils.TimeUtils

class ManageRequestsAdapter(
    private val onAcceptClick: (RequestWithDetails) -> Unit,
    private val onRejectClick: (RequestWithDetails) -> Unit
) : RecyclerView.Adapter<ManageRequestsAdapter.ViewHolder>() {

    private val items = mutableListOf<RequestWithDetails>()

    fun submitList(list: List<RequestWithDetails>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemManageRequestBinding.inflate(
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

    inner class ViewHolder(
        private val binding: ItemManageRequestBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RequestWithDetails) {
            binding.tvFoodName.text = item.donation.foodName
            binding.tvRequesterName.text = "Requester: ${item.requester.fullName}"
            binding.tvMessage.text = item.request.message
            binding.tvTime.text = TimeUtils.formatDateTime(item.request.requestedAt)
            binding.chipStatus.text = item.request.status

            val pending = item.request.status == "PENDING"
            binding.btnAccept.isEnabled = pending
            binding.btnReject.isEnabled = pending

            binding.btnAccept.setOnClickListener { onAcceptClick(item) }
            binding.btnReject.setOnClickListener { onRejectClick(item) }
        }
    }
}