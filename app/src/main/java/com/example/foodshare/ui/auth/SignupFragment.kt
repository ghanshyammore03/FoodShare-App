package com.example.foodshare.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.foodshare.FoodShareApp
import com.example.foodshare.R
import com.example.foodshare.databinding.FragmentSignupBinding
import com.example.foodshare.utils.SessionManager
import com.example.foodshare.viewmodel.AuthViewModel
import com.example.foodshare.viewmodel.AuthViewModelFactory

class SignupFragment : Fragment(R.layout.fragment_signup) {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            (requireActivity().application as FoodShareApp).userRepository
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignupBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        binding.btnSignup.setOnClickListener {
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.signup(
                fullName = binding.etFullName.text.toString(),
                email = binding.etEmail.text.toString(),
                password = password,
                phone = binding.etPhone.text.toString(),
                address = binding.etAddress.text.toString()
            )
        }

        binding.tvGoToLogin.setOnClickListener {
            findNavController().navigateUp()
        }

        observeAuthState()
    }

    private fun observeAuthState() {
        authViewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthViewModel.AuthState.Success -> {
                    sessionManager.saveLoginSession(
                        state.user.userId,
                        state.user.fullName,
                        state.user.email
                    )
                    Toast.makeText(requireContext(), "Account created successfully.", Toast.LENGTH_SHORT).show()
                    authViewModel.resetState()
                    findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
                }

                is AuthViewModel.AuthState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    authViewModel.resetState()
                }

                AuthViewModel.AuthState.Idle -> Unit
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}