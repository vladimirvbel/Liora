package com.losindomables.liora.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.losindomables.liora.domain.model.AuthError
import com.losindomables.liora.domain.model.AuthResult
import com.losindomables.liora.domain.repository.AuthRepository
import com.losindomables.liora.domain.validation.AuthValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el estado de las pantallas de Login y Registro.
 */
class AuthViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name, nameError = null, generalError = null) }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, generalError = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, generalError = null) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, confirmPasswordError = null, generalError = null) }
    }

    fun onAcceptTermsChanged(accept: Boolean) {
        _uiState.update { it.copy(acceptTerms = accept, termsError = null, generalError = null) }
    }

    fun onLoginSubmitted() {
        if (_uiState.value.isLoading) return // Ignora toques repetidos mientras carga

        val email = _uiState.value.email
        val password = _uiState.value.password

        val emailErr = AuthValidator.validateEmail(email)?.message
        val passwordErr = AuthValidator.validatePassword(password)?.message

        if (emailErr != null || passwordErr != null) {
            _uiState.update {
                it.copy(
                    emailError = emailErr,
                    passwordError = passwordErr
                )
            }
            return
        }

        performAuth {
            authRepository.login(
                email = AuthValidator.normalizeEmail(email),
                password = password
            )
        }
    }

    fun onRegisterSubmitted() {
        if (_uiState.value.isLoading) return // Ignora toques repetidos mientras carga

        val name = _uiState.value.name
        val email = _uiState.value.email
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword
        val acceptTerms = _uiState.value.acceptTerms

        val nameErr = AuthValidator.validateName(name)?.message
        val emailErr = AuthValidator.validateEmail(email)?.message
        val passwordErr = AuthValidator.validatePassword(password)?.message
        val confirmPasswordErr = if (password != confirmPassword) "Las contraseñas no coinciden" else null
        val termsErr = if (!acceptTerms) "Debes aceptar los términos y condiciones" else null

        if (nameErr != null || emailErr != null || passwordErr != null || confirmPasswordErr != null || termsErr != null) {
            _uiState.update {
                it.copy(
                    nameError = nameErr,
                    emailError = emailErr,
                    passwordError = passwordErr,
                    confirmPasswordError = confirmPasswordErr,
                    termsError = termsErr
                )
            }
            return
        }

        performAuth {
            authRepository.register(
                name = name.trim(),
                email = AuthValidator.normalizeEmail(email),
                password = password
            )
        }
    }

    fun clearGeneralError() {
        _uiState.update { it.copy(generalError = null) }
    }

    private fun performAuth(action: suspend () -> AuthResult) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            when (val result = action()) {
                is AuthResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            session = result.session,
                            generalError = null
                        )
                    }
                }
                is AuthResult.Failure -> {
                    val errorMsg = mapAuthErrorToMessage(result.error)
                    _uiState.update { state ->
                        when (result.error) {
                            is AuthError.EmailAlreadyRegistered -> state.copy(
                                isLoading = false,
                                emailError = errorMsg
                            )
                            is AuthError.InvalidCredentials -> state.copy(
                                isLoading = false,
                                generalError = errorMsg
                            )
                            is AuthError.ValidationError -> state.copy(
                                isLoading = false,
                                generalError = result.error.message
                            )
                            is AuthError.NetworkError, is AuthError.Unknown -> state.copy(
                                isLoading = false,
                                generalError = errorMsg
                            )
                        }
                    }
                }
            }
        }
    }

    private fun mapAuthErrorToMessage(error: AuthError): String {
        return when (error) {
            is AuthError.EmailAlreadyRegistered -> "Este correo electrónico ya está registrado."
            is AuthError.InvalidCredentials -> "Correo electrónico o contraseña incorrectos."
            is AuthError.ValidationError -> error.message
            is AuthError.NetworkError -> "No hay conexión a internet. Verifica tu red."
            is AuthError.Unknown -> error.message ?: "Ocurrió un error inesperado. Intenta de nuevo."
        }
    }

    companion object {
        fun Factory(repository: AuthRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AuthViewModel(repository) as T
                }
            }
    }
}
