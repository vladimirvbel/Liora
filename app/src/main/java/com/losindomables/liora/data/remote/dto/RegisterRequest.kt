package com.losindomables.liora.data.remote.dto

/**
 * Cuerpo de la petición POST /api/auth/register.
 * El password debe tener mínimo 8 caracteres (validado por el backend).
 */
data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String
)
