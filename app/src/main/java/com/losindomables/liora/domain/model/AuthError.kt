package com.losindomables.liora.domain.model

/**
 * Errores de dominio cerrados que pueden ocurrir durante la autenticación.
 */
sealed interface AuthError {
    data object EmailAlreadyRegistered : AuthError
    data object InvalidCredentials : AuthError
    data class ValidationError(val message: String) : AuthError
    data object NetworkError : AuthError
    data class Unknown(val message: String? = null) : AuthError
}
