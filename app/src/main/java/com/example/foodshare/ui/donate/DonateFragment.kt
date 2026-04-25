package com.example.foodshare.ui.donate

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.foodshare.FoodShareApp
import com.example.foodshare.R
import com.example.foodshare.databinding.FragmentDonateBinding
import com.example.foodshare.utils.FileUtils
import com.example.foodshare.utils.SessionManager
import com.example.foodshare.utils.TimeUtils
import com.example.foodshare.viewmodel.DonateViewModel
import com.example.foodshare.viewmodel.DonateViewModelFactory
import java.util.Calendar

class DonateFragment : Fragment(R.layout.fragment_donate) {

    private var _binding: FragmentDonateBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private var selectedImageUri: Uri? = null
    private var savedImagePath: String? = null
    private var selectedExpiryTime: Long = System.currentTimeMillis() + 60 * 60 * 1000

    private val donateViewModel: DonateViewModel by viewModels {
        DonateViewModelFactory(
            (requireActivity().application as FoodShareApp).donationRepository
        )
    }

    private val photoPickerLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            binding.ivSelectedFood.setImageURI(uri)
            savedImagePath = FileUtils.copyImageToInternalStorage(requireContext(), uri)
            if (savedImagePath == null) {
                Toast.makeText(requireContext(), "Failed to save image locally.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDonateBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        updateExpiryPreview()

        binding.btnPickImage.setOnClickListener {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.etExpiryTime.setOnClickListener {
            showDateTimePicker()
        }

        binding.btnSubmitDonation.setOnClickListener {
            val userId = sessionManager.getLoggedInUserId()
            if (userId <= 0) {
                Toast.makeText(requireContext(), "Please login again.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            donateViewModel.donateFood(
                donorUserId = userId,
                foodName = binding.etFoodName.text.toString(),
                quantity = binding.etQuantity.text.toString(),
                locationText = binding.etLocation.text.toString(),
                expiryTime = selectedExpiryTime,
                imagePath = savedImagePath,
                description = binding.etDescription.text.toString()
            )
        }

        observeDonateState()
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(year, month, dayOfMonth, hourOfDay, minute)
                        selectedExpiryTime = selectedCalendar.timeInMillis
                        updateExpiryPreview()
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateExpiryPreview() {
        if (_binding != null) {
            binding.etExpiryTime.setText(TimeUtils.formatDateTime(selectedExpiryTime))
        }
    }

    private fun observeDonateState() {
        donateViewModel.donateState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DonateViewModel.DonateState.Success -> {
                    Toast.makeText(requireContext(), "Donation added successfully.", Toast.LENGTH_SHORT).show()
                    clearForm()
                    donateViewModel.resetState()
                }

                is DonateViewModel.DonateState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    donateViewModel.resetState()
                }

                DonateViewModel.DonateState.Idle -> Unit
            }
        }
    }

    private fun clearForm() {
        binding.etFoodName.text?.clear()
        binding.etQuantity.text?.clear()
        binding.etLocation.text?.clear()
        binding.etDescription.text?.clear()
        selectedImageUri = null
        savedImagePath = null
        binding.ivSelectedFood.setImageResource(R.drawable.ic_food_placeholder)
        selectedExpiryTime = System.currentTimeMillis() + 60 * 60 * 1000
        updateExpiryPreview()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}