package com.losindomables.liora.ui.auth

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.losindomables.liora.ui.components.LioraLogo
import com.losindomables.liora.ui.components.LioraPasswordField
import com.losindomables.liora.ui.components.LioraPrimaryButton
import com.losindomables.liora.ui.components.LioraTextField
import com.losindomables.liora.ui.theme.LioraTheme

@Composable
fun RegisterScreen(
    uiState: AuthUiState,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.generalError) {
        uiState.generalError?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LioraLogo()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Aprende, crea, viste.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Crear Cuenta",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(16.dp))

                LioraTextField(
                    value = uiState.name,
                    onValueChange = onNameChanged,
                    label = "Nombre Completo",
                    errorMessage = uiState.nameError,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(12.dp))

                LioraTextField(
                    value = uiState.email,
                    onValueChange = onEmailChanged,
                    label = "Correo Electrónico",
                    errorMessage = uiState.emailError,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(12.dp))

                LioraPasswordField(
                    value = uiState.password,
                    onValueChange = onPasswordChanged,
                    label = "Contraseña",
                    errorMessage = uiState.passwordError,
                    imeAction = ImeAction.Done,
                    onImeAction = onRegisterClick,
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))

                LioraPrimaryButton(
                    text = "Registrarse",
                    onClick = onRegisterClick,
                    isLoading = uiState.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = onNavigateToLogin,
                    enabled = !uiState.isLoading
                ) {
                    Text(
                        text = "¿Ya tienes una cuenta? Inicia sesión aquí",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(name = "Register Screen - Modo Claro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun RegisterScreenLightPreview() {
    LioraTheme(darkTheme = false) {
        RegisterScreen(
            uiState = AuthUiState(name = "Vanesa", email = "vanesa@liora.com"),
            onNameChanged = {},
            onEmailChanged = {},
            onPasswordChanged = {},
            onRegisterClick = {},
            onNavigateToLogin = {}
        )
    }
}

@Preview(name = "Register Screen - Modo Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RegisterScreenDarkPreview() {
    LioraTheme(darkTheme = true) {
        RegisterScreen(
            uiState = AuthUiState(name = "Vanesa", email = "vanesa@liora.com"),
            onNameChanged = {},
            onEmailChanged = {},
            onPasswordChanged = {},
            onRegisterClick = {},
            onNavigateToLogin = {}
        )
    }
}
