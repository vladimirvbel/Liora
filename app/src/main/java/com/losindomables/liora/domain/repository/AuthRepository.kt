package com.losindomables.liora.domain.repository

import com.losindomables.liora.domain.model.AuthResult
import com.losindomables.liora.domain.model.AuthSession
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio de autenticación (Inversión de dependencias - SOLID).
 */
interface AuthRepository {

    /** Emite la sesión activa actual o `null` si no hay sesión. */
    val activeSession: Flow<AuthSession?>

    /** Registra a un nuevo usuario. */
    suspend fun register(name: String, email: String, password: String): AuthResult

    /** Inicia sesión con las credenciales dadas. */
    suspend fun login(email: String, password: String): AuthResult

    /** Cierra la sesión activa. */
    suspend fun logout()
}
