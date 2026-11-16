package com.example.impahla.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.impahla.data.local.UserEntity
import com.example.impahla.data.repository.UserRepository
import com.example.impahla.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val fullName: String = "",
    val role: UserRole = UserRole.CONTRIBUTOR,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loggedInUser: UserEntity? = null,
    val isRegistered: Boolean = false
)

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, errorMessage = null) }
    }

    fun onFullNameChanged(fullName: String) {
        _uiState.update { it.copy(fullName = fullName, errorMessage = null) }
    }

    fun onRoleChanged(role: UserRole) {
        _uiState.update { it.copy(role = role) }
    }

    fun login(onSuccess: (UserRole) -> Unit) {
        val state = _uiState.value
        val email = state.email.trim()
        val password = state.password.trim()

        if (email.isEmpty() || password.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Please fill in all fields") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val user = userRepository.getUserByEmail(email)
            if (user != null && user.passwordHash == password) {
                _uiState.update { it.copy(isLoading = false, loggedInUser = user) }
                onSuccess(user.role)
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Invalid email or password") }
            }
        }
    }

    fun register(onSuccess: (UserRole) -> Unit) {
        val state = _uiState.value
        val fullName = state.fullName.trim()
        val email = state.email.trim()
        val password = state.password.trim()
        val confirmPassword = state.confirmPassword.trim()

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Please fill in all fields") }
            return
        }

        if (password != confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Passwords do not match") }
            return
        }

        if (password.length < 6) {
            _uiState.update { it.copy(errorMessage = "Password must be at least 6 characters long") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val existing = userRepository.getUserByEmail(email)
            if (existing != null) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Email is already registered") }
                return@launch
            }

            val newUser = UserEntity(
                fullName = fullName,
                email = email,
                passwordHash = password,
                role = state.role
            )
            val userId = userRepository.registerUser(newUser)
            val createdUser = newUser.copy(userId = userId)
            _uiState.update { it.copy(isLoading = false, loggedInUser = createdUser, isRegistered = true) }
            onSuccess(createdUser.role)
        }
    }
}

class AuthViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
