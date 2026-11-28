package com.losindomables.liora.domain.model

/**
 * Resultado cerrado de una operación de autenticación.
 */
sealed interface AuthResult {
    data class Success(val session: AuthSession) : AuthResult
    data class Failure(val error: AuthError) : AuthResult
}
