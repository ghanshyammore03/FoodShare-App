package com.example.foodshare.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.foodshare.FoodShareApp
import com.example.foodshare.R
import com.example.foodshare.databinding.FragmentLoginBinding
import com.example.foodshare.utils.SessionManager
import com.example.foodshare.viewmodel.AuthViewModel
import com.example.foodshare.viewmodel.AuthViewModelFactory

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            (requireActivity().application as FoodShareApp).userRepository
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        if (sessionManager.isLoggedIn()) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            return
        }

        binding.btnLogin.setOnClickListener {
            authViewModel.login(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        binding.tvGoToSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
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
                    Toast.makeText(requireContext(), "Welcome ${state.user.fullName}", Toast.LENGTH_SHORT).show()
                    authViewModel.resetState()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
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