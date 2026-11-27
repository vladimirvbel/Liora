package com.losindomables.liora.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.losindomables.liora.domain.repository.AuthRepository
import com.losindomables.liora.ui.auth.AuthViewModel
import com.losindomables.liora.ui.auth.LoginScreen
import com.losindomables.liora.ui.auth.RegisterScreen
import com.losindomables.liora.ui.components.LioraPrimaryButton
import kotlinx.coroutines.launch

object AuthRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun AppNavigation(
    authRepository: AuthRepository,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onAuthSuccess: () -> Unit = {},
) {
    val activeSession by authRepository.activeSession.collectAsState(initial = null)
    val scope = rememberCoroutineScope()

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.Factory(authRepository),
    )

    val uiState by authViewModel.uiState.collectAsState()

    // Si ya existe sesión guardada o recién iniciada, va directo a HOME limpiando la pila de Auth
    LaunchedEffect(activeSession, uiState.session) {
        if ((activeSession != null) || (uiState.session != null)) {
            onAuthSuccess()
            navController.navigate(AuthRoutes.HOME) {
                popUpTo(AuthRoutes.LOGIN) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = AuthRoutes.LOGIN,
        modifier = modifier
    ) {
        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                uiState = uiState,
                onEmailChanged = authViewModel::onEmailChanged,
                onPasswordChanged = authViewModel::onPasswordChanged,
                onLoginClick = authViewModel::onLoginSubmitted,
                onNavigateToRegister = {
                    authViewModel.clearGeneralError()
                    navController.navigate(AuthRoutes.REGISTER)
                }
            )
        }

        composable(AuthRoutes.REGISTER) {
            RegisterScreen(
                uiState = uiState,
                onNameChanged = authViewModel::onNameChanged,
                onEmailChanged = authViewModel::onEmailChanged,
                onPasswordChanged = authViewModel::onPasswordChanged,
                onRegisterClick = authViewModel::onRegisterSubmitted,
                onNavigateToLogin = {
                    authViewModel.clearGeneralError()
                    navController.popBackStack()
                }
            )
        }

        composable(AuthRoutes.HOME) {
            HomeScreen(
                role = activeSession?.role?.name ?: uiState.session?.role?.name ?: "CLIENTE",
                onLogoutClick = {
                    scope.launch {
                        authRepository.logout()
                        navController.navigate(AuthRoutes.LOGIN) {
                            popUpTo(AuthRoutes.HOME) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeScreen(
    role: String,
    onLogoutClick: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¡Bienvenido a Liora!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Rol de usuario: $role",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(32.dp))
            LioraPrimaryButton(
                text = "Cerrar Sesión",
                onClick = onLogoutClick
            )
        }
    }
}
