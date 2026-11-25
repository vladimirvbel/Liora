package com.losindomables.liora.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.losindomables.liora.domain.repository.AuthRepository
import com.losindomables.liora.ui.auth.AuthViewModel
import com.losindomables.liora.ui.auth.LoginScreen
import com.losindomables.liora.ui.auth.RegisterScreen

object AuthRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
}

@Composable
fun AppNavigation(
    authRepository: AuthRepository,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onAuthSuccess: () -> Unit = {},
) {
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.Factory(authRepository)
    )

    val uiState by authViewModel.uiState.collectAsState()

    if (uiState.session != null) {
        onAuthSuccess()
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
    }
}
