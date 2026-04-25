package com.example.foodshare.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodshare.FoodShareApp
import com.example.foodshare.R
import com.example.foodshare.databinding.FragmentHomeBinding
import com.example.foodshare.utils.SessionManager
import com.example.foodshare.viewmodel.HomeViewModel
import com.example.foodshare.viewmodel.HomeViewModelFactory

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var donationAdapter: DonationAdapter

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            (requireActivity().application as FoodShareApp).donationRepository
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        val userName = sessionManager.getLoggedInUserName().ifBlank { "User" }
        binding.tvWelcome.text = "Welcome, $userName"
        binding.tvSubtitle.text = getString(R.string.home_subtitle_message)

        setupRecyclerView()
        observeDonations()
    }

    private fun setupRecyclerView() {
        donationAdapter = DonationAdapter { donationWithDonor ->
            val bundle = Bundle().apply {
                putLong("donationId", donationWithDonor.donation.donationId)
            }
            findNavController().navigate(R.id.action_homeFragment_to_foodDetailsFragment, bundle)
        }

        binding.recyclerDonations.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = donationAdapter
        }
    }

    private fun observeDonations() {
        homeViewModel.donations.observe(viewLifecycleOwner) { donations ->
            donationAdapter.submitList(donations)

            val isEmpty = donations.isEmpty()
            binding.recyclerDonations.visibility = if (isEmpty) View.GONE else View.VISIBLE
            binding.tvEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}