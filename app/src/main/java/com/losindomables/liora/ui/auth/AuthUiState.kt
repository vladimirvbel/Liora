package com.losindomables.liora.ui.auth

import com.losindomables.liora.domain.model.AuthSession

/**
 * Estado inmutable de la interfaz de usuario para Login y Registro.
 */
data class AuthUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val acceptTerms: Boolean = false,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val termsError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false,
    val session: AuthSession? = null,
)
