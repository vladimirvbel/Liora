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
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.losindomables.liora.R
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
    onConfirmPasswordChanged: (String) -> Unit,
    onAcceptTermsChanged: (Boolean) -> Unit,
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
                    text = "Crea tu cuenta",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Comienza tu experiencia en Liora",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))
                
                val isDark = isSystemInDarkTheme()
                val cardColor = if (isDark) Color(0xFF1E1D1D) else Color.White
                
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = cardColor,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LioraTextField(
                            value = uiState.name,
                            onValueChange = onNameChanged,
                            label = "Nombre completo",
                            placeholder = "Tu nombre y apellido",
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_profile),
                                    contentDescription = "Icono de perfil",
                                    tint = if(isDark) Color.White else Color.Black
                                )
                            },
                            errorMessage = uiState.nameError,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            enabled = !uiState.isLoading
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LioraTextField(
                            value = uiState.email,
                            onValueChange = onEmailChanged,
                            label = "Correo electrónico",
                            placeholder = "ejemplo@correo.com",
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_mail),
                                    contentDescription = "Icono de correo",
                                    tint = if(isDark) Color.White else Color.Black
                                )
                            },
                            errorMessage = uiState.emailError,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                            enabled = !uiState.isLoading
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LioraPasswordField(
                            value = uiState.password,
                            onValueChange = onPasswordChanged,
                            label = "Contraseña",
                            placeholder = "Mínimo 8 caracteres",
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_lock),
                                    contentDescription = "Icono de candado",
                                    tint = if(isDark) Color.White else Color.Black
                                )
                            },
                            errorMessage = uiState.passwordError,
                            imeAction = ImeAction.Next,
                            enabled = !uiState.isLoading
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        LioraPasswordField(
                            value = uiState.confirmPassword,
                            onValueChange = onConfirmPasswordChanged,
                            label = "Confirmar contraseña",
                            placeholder = "Repite tu contraseña",
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_lock),
                                    contentDescription = "Icono de candado",
                                    tint = if(isDark) Color.White else Color.Black
                                )
                            },
                            errorMessage = uiState.confirmPasswordError,
                            imeAction = ImeAction.Done,
                            onImeAction = onRegisterClick,
                            enabled = !uiState.isLoading
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = uiState.acceptTerms,
                                onCheckedChange = onAcceptTermsChanged,
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                    uncheckedColor = if (uiState.termsError != null) MaterialTheme.colorScheme.error else Color.LightGray
                                )
                            )
                            
                            val termsText = buildAnnotatedString {
                                append("Acepto los ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline, color = if(isDark) Color.White else Color.Black)) {
                                    append("Términos y Condiciones")
                                }
                                append(" y la ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline, color = if(isDark) Color.White else Color.Black)) {
                                    append("Política de Privacidad")
                                }
                            }
                            
                            Text(
                                text = termsText,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (uiState.termsError != null) MaterialTheme.colorScheme.error else Color.Gray,
                                modifier = Modifier.padding(start = 4.dp).clickable { onAcceptTermsChanged(!uiState.acceptTerms) }
                            )
                        }

                        if (uiState.termsError != null) {
                            Text(
                                text = uiState.termsError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp).fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        LioraPrimaryButton(
                            text = "Crear cuenta ->", // O puedes usar un IconButton real si lo prefieres
                            onClick = onRegisterClick,
                            isLoading = uiState.isLoading
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "¿Ya tienes una cuenta? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Inicia sesión",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 4.dp).clickable { onNavigateToLogin() }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Liora aprende, crea, viste",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
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
            onConfirmPasswordChanged = {},
            onAcceptTermsChanged = {},
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
            onConfirmPasswordChanged = {},
            onAcceptTermsChanged = {},
            onRegisterClick = {},
            onNavigateToLogin = {}
        )
    }
}
