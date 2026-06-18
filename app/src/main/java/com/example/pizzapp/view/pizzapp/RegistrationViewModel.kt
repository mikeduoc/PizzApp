package com.example.pizzapp.view.pizzapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed interface RegistrationUiState {
    object Idle : RegistrationUiState
    object Loading : RegistrationUiState
    object Success : RegistrationUiState
    data class Error(val message: String) : RegistrationUiState
}

class RegistrationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun createUser(email: String, password: String, context: Context) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = RegistrationUiState.Error("El correo electrónico y la contraseña no pueden estar vacíos.")
            return
        }

        viewModelScope.launch {
            _uiState.value = RegistrationUiState.Loading
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _uiState.value = RegistrationUiState.Success
            } catch (e: Exception) {
                _uiState.value = RegistrationUiState.Error(e.message ?: "Ocurrió un error desconocido.")
            }
        }
    }

    fun resetState() {
        _uiState.value = RegistrationUiState.Idle
    }
}
