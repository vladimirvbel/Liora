package com.losindomables.liora.data.remote.dto

/**
 * Cuerpo de la petición POST /api/auth/login.
 */
data class LoginRequest(
    val email: String,
    val password: String
)
