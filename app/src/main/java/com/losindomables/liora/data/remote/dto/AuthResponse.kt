package com.losindomables.liora.data.remote.dto

/**
 * Respuesta (200 OK) de /api/auth/register y /api/auth/login.
 * [role] llega como texto: "CLIENTE", "VENDEDOR" o "ADMIN".
 */
data class AuthResponse(
    val token: String,
    val role: String
)
