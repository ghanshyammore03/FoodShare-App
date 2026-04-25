package com.example.foodshare.ui.details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.foodshare.FoodShareApp
import com.example.foodshare.R
import com.example.foodshare.databinding.FragmentFoodDetailsBinding
import com.example.foodshare.utils.TimeUtils
import com.example.foodshare.viewmodel.HomeViewModel
import com.example.foodshare.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.launch

class FoodDetailsFragment : Fragment(R.layout.fragment_food_details) {

    private var _binding: FragmentFoodDetailsBinding? = null
    private val binding get() = _binding!!
    private var currentDonationId: Long = -1L

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            (requireActivity().application as FoodShareApp).donationRepository
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFoodDetailsBinding.bind(view)

        val donationId = arguments?.getLong("donationId", -1L) ?: -1L
        currentDonationId = donationId

        if (donationId == -1L) {
            Toast.makeText(requireContext(), "Invalid donation selected.", Toast.LENGTH_SHORT).show()
            return
        }

        loadDonationDetails(donationId)

        binding.btnRequestFood.setOnClickListener {
            RequestFoodDialogFragment.newInstance(currentDonationId)
                .show(parentFragmentManager, "RequestFoodDialog")
        }
    }

    private fun loadDonationDetails(donationId: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            val item = homeViewModel.getDonationById(donationId)

            if (item == null) {
                Toast.makeText(requireContext(), "Donation not found.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            binding.tvFoodName.text = item.donation.foodName
            binding.tvQuantity.text = "Quantity: ${item.donation.quantity}"
            binding.tvLocation.text = "Pickup Location: ${item.donation.locationText}"
            binding.tvExpiry.text = "Expiry Time: ${TimeUtils.formatDateTime(item.donation.expiryTime)}"
            binding.tvDonorName.text = "Donor: ${item.donor.fullName}"
            binding.tvDonorEmail.text = "Email: ${item.donor.email}"
            binding.tvDescription.text = if (item.donation.description.isBlank()) {
                "No additional description provided."
            } else {
                item.donation.description
            }

            if (!item.donation.imagePath.isNullOrBlank()) {
                binding.ivFood.load(item.donation.imagePath) {
                    placeholder(R.drawable.ic_food_placeholder)
                    error(R.drawable.ic_food_placeholder)
                }
            } else {
                binding.ivFood.setImageResource(R.drawable.ic_food_placeholder)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}