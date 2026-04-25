package com.example.foodshare.ui.requests

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodshare.FoodShareApp
import com.example.foodshare.R
import com.example.foodshare.databinding.FragmentManageRequestsBinding
import com.example.foodshare.utils.SessionManager
import com.example.foodshare.viewmodel.ManageRequestsViewModel
import com.example.foodshare.viewmodel.ManageRequestsViewModelFactory

class ManageRequestsFragment : Fragment(R.layout.fragment_manage_requests) {

    private var _binding: FragmentManageRequestsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: ManageRequestsAdapter

    private val viewModel: ManageRequestsViewModel by viewModels {
        ManageRequestsViewModelFactory(
            (requireActivity().application as FoodShareApp).foodRequestRepository,
            (requireActivity().application as FoodShareApp).donationRepository
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentManageRequestsBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        setupRecyclerView()
        observeData()

        val donorUserId = sessionManager.getLoggedInUserId()
        if (donorUserId > 0) {
            viewModel.loadRequestsForDonor(donorUserId)
        }
    }

    private fun setupRecyclerView() {
        adapter = ManageRequestsAdapter(
            onAcceptClick = { item ->
                showAcceptDialog(item.request.requestId, item.request.donationId)
            },
            onRejectClick = { item ->
                showRejectDialog(item.request.requestId)
            }
        )

        binding.recyclerRequests.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRequests.adapter = adapter
    }

    private fun observeData() {
        val donorUserId = sessionManager.getLoggedInUserId()

        viewModel.requests.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvEmptyRequests.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.actionState.observe(viewLifecycleOwner) { message ->
            if (message.isNotBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModel.loadRequestsForDonor(donorUserId)
            }
        }
    }

    private fun showAcceptDialog(requestId: Long, donationId: Long) {
        AlertDialog.Builder(requireContext())
            .setTitle("Accept Request")
            .setMessage("Are you sure you want to accept this request?")
            .setPositiveButton("Accept") { _, _ ->
                viewModel.acceptRequest(
                    requestId = requestId,
                    donationId = donationId,
                    donorUserId = sessionManager.getLoggedInUserId()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showRejectDialog(requestId: Long) {
        AlertDialog.Builder(requireContext())
            .setTitle("Reject Request")
            .setMessage("Are you sure you want to reject this request?")
            .setPositiveButton("Reject") { _, _ ->
                viewModel.rejectRequest(
                    requestId = requestId,
                    donorUserId = sessionManager.getLoggedInUserId()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}