package com.losindomables.liora.data.local

import kotlinx.coroutines.flow.Flow

/**
 * Interfaz para persistir la sesión del usuario (token JWT y rol) en el dispositivo.
 */
interface SessionManager {
    val token: Flow<String?>
    val role: Flow<String?>
    suspend fun saveSession(token: String, role: String)
    suspend fun clearSession()
}
