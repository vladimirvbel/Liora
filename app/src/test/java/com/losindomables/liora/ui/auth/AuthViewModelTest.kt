package com.losindomables.liora.ui.auth

import com.losindomables.liora.domain.model.AuthError
import com.losindomables.liora.domain.model.AuthResult
import com.losindomables.liora.domain.model.AuthSession
import com.losindomables.liora.domain.model.Role
import com.losindomables.liora.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeAuthRepository
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeAuthRepository()
        viewModel = AuthViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onLoginSubmitted with invalid inputs updates field errors without calling repository`() = runTest {
        viewModel.onEmailChanged("invalid-email")
        viewModel.onPasswordChanged("short")

        viewModel.onLoginSubmitted()

        val state = viewModel.uiState.value
        assertNotNull(state.emailError)
        assertNotNull(state.passwordError)
        assertEquals(0, fakeRepository.loginCallCount)
    }

    @Test
    fun `onLoginSubmitted with valid inputs calls repository and updates state on success`() = runTest {
        viewModel.onEmailChanged("user@test.com")
        viewModel.onPasswordChanged("password123")
        fakeRepository.loginResult = AuthResult.Success(AuthSession("jwt-token", Role.CLIENTE))

        viewModel.onLoginSubmitted()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.session)
        assertEquals("jwt-token", state.session?.token)
        assertEquals(1, fakeRepository.loginCallCount)
    }

    @Test
    fun `onLoginSubmitted ignores duplicate calls while loading`() = runTest {
        viewModel.onEmailChanged("user@test.com")
        viewModel.onPasswordChanged("password123")

        viewModel.onLoginSubmitted()
        viewModel.onLoginSubmitted() // Duplicate call while loading

        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, fakeRepository.loginCallCount)
    }

    @Test
    fun `onRegisterSubmitted with invalid name sets nameError`() = runTest {
        viewModel.onNameChanged("")
        viewModel.onEmailChanged("user@test.com")
        viewModel.onPasswordChanged("password123")

        viewModel.onRegisterSubmitted()

        val state = viewModel.uiState.value
        assertNotNull(state.nameError)
        assertEquals(0, fakeRepository.registerCallCount)
    }

    @Test
    fun `onLoginSubmitted updates generalError on InvalidCredentials`() = runTest {
        viewModel.onEmailChanged("user@test.com")
        viewModel.onPasswordChanged("wrongpassword")
        fakeRepository.loginResult = AuthResult.Failure(AuthError.InvalidCredentials)

        viewModel.onLoginSubmitted()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Correo electrónico o contraseña incorrectos.", state.generalError)
        assertNull(state.session)
    }
}

private class FakeAuthRepository : AuthRepository {
    var loginCallCount = 0
    var registerCallCount = 0
    var loginResult: AuthResult = AuthResult.Failure(AuthError.NetworkError)
    var registerResult: AuthResult = AuthResult.Failure(AuthError.NetworkError)

    override val activeSession: Flow<AuthSession?> = flowOf(null)

    override suspend fun register(name: String, email: String, password: String): AuthResult {
        registerCallCount++
        return registerResult
    }

    override suspend fun login(email: String, password: String): AuthResult {
        loginCallCount++
        return loginResult
    }

    override suspend fun logout() {}
}
