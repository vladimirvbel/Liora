package com.losindomables.liora.data.repository

import com.losindomables.liora.data.local.SessionManager
import com.losindomables.liora.data.remote.AuthApi
import com.losindomables.liora.domain.model.AuthError
import com.losindomables.liora.domain.model.AuthResult
import com.losindomables.liora.domain.model.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AuthRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fakeSessionManager = FakeSessionManager()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var authApi: AuthApi
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        authApi = retrofit.create(AuthApi::class.java)
        repository = AuthRepositoryImpl(authApi, fakeSessionManager, testDispatcher)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `login returns Success 200 and saves session`() = runTest(testDispatcher) {
        val jsonResponse = """
            {
                "token": "jwt-mock-token-123",
                "role": "CLIENTE"
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(jsonResponse))

        val result = repository.login("user@test.com", "password123")

        assertTrue(result is AuthResult.Success)
        val success = result as AuthResult.Success
        assertEquals("jwt-mock-token-123", success.session.token)
        assertEquals(Role.CLIENTE, success.session.role)
        assertEquals("jwt-mock-token-123", fakeSessionManager.savedToken)
        assertEquals("CLIENTE", fakeSessionManager.savedRole)
    }

    @Test
    fun `login returns InvalidCredentials on HTTP 401`() = runTest(testDispatcher) {
        mockWebServer.enqueue(MockResponse().setResponseCode(401).setBody("""{"error":"Unauthorized"}"""))

        val result = repository.login("user@test.com", "wrongpass")

        assertTrue(result is AuthResult.Failure)
        assertEquals(AuthError.InvalidCredentials, (result as AuthResult.Failure).error)
    }

    @Test
    fun `register returns EmailAlreadyRegistered on HTTP 409`() = runTest(testDispatcher) {
        mockWebServer.enqueue(MockResponse().setResponseCode(409).setBody("""{"error":"Email conflict"}"""))

        val result = repository.register("Vanesa", "existing@test.com", "password123")

        assertTrue(result is AuthResult.Failure)
        assertEquals(AuthError.EmailAlreadyRegistered, (result as AuthResult.Failure).error)
    }

    @Test
    fun `register returns ValidationError on HTTP 400`() = runTest(testDispatcher) {
        val errorMsg = "La contraseña debe contener letras y números."
        mockWebServer.enqueue(MockResponse().setResponseCode(400).setBody(errorMsg))

        val result = repository.register("Vanesa", "invalid@test.com", "short")

        assertTrue(result is AuthResult.Failure)
        val failure = result as AuthResult.Failure
        assertTrue(failure.error is AuthError.ValidationError)
        assertEquals(errorMsg, (failure.error as AuthError.ValidationError).message)
    }

    @Test
    fun `login returns NetworkError on socket timeout`() = runTest(testDispatcher) {
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))

        val result = repository.login("user@test.com", "password123")

        assertTrue(result is AuthResult.Failure)
        assertEquals(AuthError.NetworkError, (result as AuthResult.Failure).error)
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
