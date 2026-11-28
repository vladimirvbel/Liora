package com.losindomables.liora.data.repository

import com.losindomables.liora.data.local.SessionManager
import com.losindomables.liora.data.remote.AuthApi
import com.losindomables.liora.data.remote.dto.AuthResponse
import com.losindomables.liora.data.remote.dto.LoginRequest
import com.losindomables.liora.data.remote.dto.RegisterRequest
import com.losindomables.liora.domain.model.AuthError
import com.losindomables.liora.domain.model.AuthResult
import com.losindomables.liora.domain.model.AuthSession
import com.losindomables.liora.domain.model.Role
import com.losindomables.liora.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

/**
 * Implementación de [AuthRepository] que coordina llamadas a la red ([AuthApi]) y persistencia local ([SessionManager]).
 *
 * @param ioDispatcher Inyectado para facilitar pruebas unitarias evitando bloqueos en el hilo principal.
 */
class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AuthRepository {

    override val activeSession: Flow<AuthSession?> = combine(
        sessionManager.token,
        sessionManager.role
    ) { token, role ->
        if ((token != null) && (role != null)) {
            AuthSession(token, Role.fromString(role))
        } else {
            null
        }
    }

    override suspend fun register(name: String, email: String, password: String): AuthResult =
        withContext(ioDispatcher) {
            try {
                val response = authApi.register(RegisterRequest(nombre = name, email = email, password = password))
                handleAuthResponse(response)
            } catch (_: IOException) {
                AuthResult.Failure(AuthError.NetworkError)
            } catch (e: Exception) {
                AuthResult.Failure(AuthError.Unknown(e.localizedMessage))
            }
        }

    override suspend fun login(email: String, password: String): AuthResult =
        withContext(ioDispatcher) {
            try {
                val response = authApi.login(LoginRequest(email = email, password = password))
                handleAuthResponse(response)
            } catch (_: IOException) {
                AuthResult.Failure(AuthError.NetworkError)
            } catch (e: Exception) {
                AuthResult.Failure(AuthError.Unknown(e.localizedMessage))
            }
        }

    override suspend fun logout() {
        withContext(ioDispatcher) {
            sessionManager.clearSession()
        }
    }

    private suspend fun handleAuthResponse(response: Response<AuthResponse>): AuthResult {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                sessionManager.saveSession(body.token, body.role)
                val role = Role.fromString(body.role)
                return AuthResult.Success(AuthSession(body.token, role))
            }
        }

        return when (response.code()) {
            409 -> AuthResult.Failure(AuthError.EmailAlreadyRegistered)
            401 -> AuthResult.Failure(AuthError.InvalidCredentials)
            400 -> {
                val errorMsg = response.errorBody()?.string()?.takeIf { it.isNotBlank() }
                    ?: "Datos de entrada no válidos."
                AuthResult.Failure(AuthError.ValidationError(errorMsg))
            }
            in 500..599 -> AuthResult.Failure(AuthError.Unknown("Error del servidor (${response.code()}). Reintente más tarde."))
            else -> AuthResult.Failure(AuthError.Unknown("Error inesperado: ${response.code()}"))
        }
    }
}
