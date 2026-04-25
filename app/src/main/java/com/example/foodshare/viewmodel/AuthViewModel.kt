package com.example.foodshare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodshare.data.local.entity.UserEntity
import com.example.foodshare.data.repository.UserRepository
import com.example.foodshare.utils.PasswordUtils
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun signup(
        fullName: String,
        email: String,
        password: String,
        phone: String,
        address: String
    ) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Name, email, and password are required.")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _authState.value = AuthState.Error("Enter a valid email address.")
            return
        }

        if (password.length < 6) {
            _authState.value = AuthState.Error("Password must be at least 6 characters.")
            return
        }

        viewModelScope.launch {
            try {
                val existingUser = userRepository.getUserByEmail(email.trim())
                if (existingUser != null) {
                    _authState.postValue(AuthState.Error("An account with this email already exists."))
                    return@launch
                }

                val user = UserEntity(
                    fullName = fullName.trim(),
                    email = email.trim(),
                    passwordHash = PasswordUtils.hashPassword(password),
                    phone = phone.trim(),
                    address = address.trim(),
                    role = "USER"
                )

                val userId = userRepository.registerUser(user)
                val savedUser = userRepository.getUserById(userId)

                if (savedUser != null) {
                    _authState.postValue(AuthState.Success(savedUser))
                } else {
                    _authState.postValue(AuthState.Error("Signup failed. Please try again."))
                }
            } catch (e: Exception) {
                _authState.postValue(AuthState.Error(e.message ?: "Signup failed."))
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password are required.")
            return
        }

        viewModelScope.launch {
            try {
                val user = userRepository.getUserByEmail(email.trim())
                if (user == null) {
                    _authState.postValue(AuthState.Error("User not found. Please sign up first."))
                    return@launch
                }

                val isValid = PasswordUtils.verifyPassword(password, user.passwordHash)
                if (!isValid) {
                    _authState.postValue(AuthState.Error("Incorrect password."))
                    return@launch
                }

                _authState.postValue(AuthState.Success(user))
            } catch (e: Exception) {
                _authState.postValue(AuthState.Error(e.message ?: "Login failed."))
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    sealed class AuthState {
        data object Idle : AuthState()
        data class Success(val user: UserEntity) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}