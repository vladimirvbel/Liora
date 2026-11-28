package com.losindomables.liora.domain.model

/**
 * Representación inmutable de la sesión activa del usuario.
 */
data class AuthSession(
    val token: String,
    val role: Role,
)
