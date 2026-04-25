package com.example.foodshare.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodshare.FoodShareApp
import com.example.foodshare.R
import com.example.foodshare.databinding.FragmentProfileBinding
import com.example.foodshare.utils.SessionManager
import com.example.foodshare.viewmodel.ProfileViewModel
import com.example.foodshare.viewmodel.ProfileViewModelFactory

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var donationHistoryAdapter: DonationHistoryAdapter
    private lateinit var requestHistoryAdapter: RequestHistoryAdapter

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(
            (requireActivity().application as FoodShareApp).userRepository,
            (requireActivity().application as FoodShareApp).donationRepository,
            (requireActivity().application as FoodShareApp).foodRequestRepository
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        setupRecyclerViews()
        observeData()

        val userId = sessionManager.getLoggedInUserId()
        if (userId > 0) {
            profileViewModel.loadProfile(userId)
        }
    }

    private fun setupRecyclerViews() {
        donationHistoryAdapter = DonationHistoryAdapter()
        requestHistoryAdapter = RequestHistoryAdapter()

        binding.recyclerDonationHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = donationHistoryAdapter
        }

        binding.recyclerRequestHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = requestHistoryAdapter
        }
    }

    private fun observeData() {
        profileViewModel.userName.observe(viewLifecycleOwner) {
            binding.tvProfileName.text = it
        }

        profileViewModel.userEmail.observe(viewLifecycleOwner) {
            binding.tvProfileEmail.text = it
        }

        profileViewModel.userAddress.observe(viewLifecycleOwner) {
            binding.tvProfileAddress.text = it.ifBlank { "No address added" }
        }

        profileViewModel.userDonations.observe(viewLifecycleOwner) {
            donationHistoryAdapter.submitList(it)
            binding.tvNoDonations.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }

        profileViewModel.userRequests.observe(viewLifecycleOwner) {
            requestHistoryAdapter.submitList(it)
            binding.tvNoRequests.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}