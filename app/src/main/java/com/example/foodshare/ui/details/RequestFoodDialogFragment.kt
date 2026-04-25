package com.example.foodshare.ui.details

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.foodshare.FoodShareApp
import com.example.foodshare.data.local.entity.FoodRequestEntity
import com.example.foodshare.databinding.DialogRequestFoodBinding
import com.example.foodshare.utils.SessionManager
import kotlinx.coroutines.launch

class RequestFoodDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogRequestFoodBinding.inflate(layoutInflater)
        val donationId = requireArguments().getLong(ARG_DONATION_ID)
        val sessionManager = SessionManager(requireContext())

        return AlertDialog.Builder(requireContext())
            .setTitle("Request Food")
            .setView(binding.root)
            .setPositiveButton("Send") { _, _ ->
                val userId = sessionManager.getLoggedInUserId()
                val message = binding.etRequestMessage.text.toString()

                if (userId <= 0L) {
                    Toast.makeText(requireContext(), "Please login again.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    val repository = (requireActivity().application as FoodShareApp).foodRequestRepository
                    repository.insertRequest(
                        FoodRequestEntity(
                            donationId = donationId,
                            requesterUserId = userId,
                            message = message.ifBlank { "Interested in this donation." },
                            status = "PENDING"
                        )
                    )
                    Toast.makeText(requireContext(), "Request sent successfully.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    companion object {
        private const val ARG_DONATION_ID = "donation_id"

        fun newInstance(donationId: Long): RequestFoodDialogFragment {
            val fragment = RequestFoodDialogFragment()
            fragment.arguments = Bundle().apply {
                putLong(ARG_DONATION_ID, donationId)
            }
            return fragment
        }
    }
}