package com.losindomables.liora.data.repository

import com.losindomables.liora.data.local.SessionManager
import com.losindomables.liora.data.remote.AuthApi
import com.losindomables.liora.data.remote.dto.AuthResponse
import com.losindomables.liora.data.remote.dto.LoginRequest
import com.losindomables.liora.domain.model.AuthError
import com.losindomables.liora.domain.model.AuthResult
import com.losindomables.liora.domain.model.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class AuthRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fakeSessionManager = FakeSessionManager()

    @Test
    fun `login success saves session and returns AuthResult Success`() = runTest(testDispatcher) {
        val fakeApi = object : FakeAuthApi() {
            override suspend fun login(request: LoginRequest): Response<AuthResponse> {
                return Response.success(AuthResponse("test-token", "CLIENTE"))
            }
        }
        val repository = AuthRepositoryImpl(fakeApi, fakeSessionManager, testDispatcher)

        val result = repository.login("user@test.com", "password123")

        assertTrue(result is AuthResult.Success)
        val success = result as AuthResult.Success
        assertEquals("test-token", success.session.token)
        assertEquals(Role.CLIENTE, success.session.role)
        assertEquals("test-token", fakeSessionManager.savedToken)
        assertEquals("CLIENTE", fakeSessionManager.savedRole)
    }

    @Test
    fun `login returns InvalidCredentials on 401 response`() = runTest(testDispatcher) {
        val fakeApi = object : FakeAuthApi() {
            override suspend fun login(request: LoginRequest): Response<AuthResponse> {
                return Response.error(401, "".toResponseBody(null))
            }
        }
        val repository = AuthRepositoryImpl(fakeApi, fakeSessionManager, testDispatcher)

        val result = repository.login("user@test.com", "wrongpass")

        assertTrue(result is AuthResult.Failure)
        assertEquals(AuthError.InvalidCredentials, (result as AuthResult.Failure).error)
    }

    @Test
    fun `register returns EmailAlreadyRegistered on 409 response`() = runTest(testDispatcher) {
        val fakeApi = object : FakeAuthApi() {
            override suspend fun register(request: com.losindomables.liora.data.remote.dto.RegisterRequest): Response<AuthResponse> {
                return Response.error(409, "".toResponseBody(null))
            }
        }
        val repository = AuthRepositoryImpl(fakeApi, fakeSessionManager, testDispatcher)

        val result = repository.register("Vanesa", "existing@test.com", "password123")

        assertTrue(result is AuthResult.Failure)
        assertEquals(AuthError.EmailAlreadyRegistered, (result as AuthResult.Failure).error)
    }

    @Test
    fun `login returns NetworkError when IOException occurs`() = runTest(testDispatcher) {
        val fakeApi = object : FakeAuthApi() {
            override suspend fun login(request: LoginRequest): Response<AuthResponse> {
                throw IOException("No internet connection")
            }
        }
        val repository = AuthRepositoryImpl(fakeApi, fakeSessionManager, testDispatcher)

        val result = repository.login("user@test.com", "password123")

        assertTrue(result is AuthResult.Failure)
        assertEquals(AuthError.NetworkError, (result as AuthResult.Failure).error)
    }
}

private open class FakeAuthApi : AuthApi {
    override suspend fun register(request: com.losindomables.liora.data.remote.dto.RegisterRequest): Response<AuthResponse> {
        throw NotImplementedError()
    }

    override suspend fun login(request: LoginRequest): Response<AuthResponse> {
        throw NotImplementedError()
    }
}

private class FakeSessionManager : SessionManager {
    var savedToken: String? = null
    var savedRole: String? = null

    private val _token = MutableStateFlow<String?>(null)
    private val _role = MutableStateFlow<String?>(null)

    override val token: Flow<String?> = _token
    override val role: Flow<String?> = _role

    override suspend fun saveSession(token: String, role: String) {
        savedToken = token
        savedRole = role
        _token.value = token
        _role.value = role
    }

    override suspend fun clearSession() {
        savedToken = null
        savedRole = null
        _token.value = null
        _role.value = null
    }
}
