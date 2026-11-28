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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.shadow
import com.losindomables.liora.R
import com.losindomables.liora.ui.components.LioraLogo
import com.losindomables.liora.ui.components.LioraPasswordField
import com.losindomables.liora.ui.components.LioraPrimaryButton
import com.losindomables.liora.ui.components.LioraTextField
import com.losindomables.liora.ui.theme.LioraTheme

@Composable
fun LoginScreen(
    uiState: AuthUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
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
                    text = "¡Bienvenido de nuevo!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Accede a tu cuenta",
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
                            value = uiState.email,
                            onValueChange = onEmailChanged,
                            label = "Correo electrónico",
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
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_lock),
                                    contentDescription = "Icono de candado",
                                    tint = if(isDark) Color.White else Color.Black
                                )
                            },
                            errorMessage = uiState.passwordError,
                            imeAction = ImeAction.Done,
                            onImeAction = onLoginClick,
                            enabled = !uiState.isLoading
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))

                        LioraPrimaryButton(
                            text = "Iniciar sesión",
                            onClick = onLoginClick,
                            isLoading = uiState.isLoading
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                            Text(
                                text = "O CONTINUAR CON",
                                modifier = Modifier.padding(horizontal = 8.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isDark) Color.LightGray else Color.DarkGray
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = { /* TODO */ },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .shadow(
                                        elevation = 4.dp,
                                        shape = RoundedCornerShape(12.dp),
                                        clip = false
                                    ),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isDark) Color(0xFF333333) else Color(0xFFF7EBEB)
                                ),
                                border = null
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // TODO: Agregar el ícono real de Google
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_google),
                                        contentDescription = "Google",
                                        tint = Color.Unspecified // <--- Esto hace que c
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Google", color = if (isDark) Color.White else Color.Black)
                                }
                            }
                            
                            OutlinedButton(
                                onClick = { /* TODO */ },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .shadow(
                                        elevation = 4.dp,
                                        shape = RoundedCornerShape(12.dp),
                                        clip = false
                                    ),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isDark) Color(0xFF333333) else Color(0xFFF7EBEB)
                                ),
                                border = null
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_apple),
                                        contentDescription = "Apple",
                                        tint = if (isDark) Color.White else Color.Black
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Apple", color = if (isDark) Color.White else Color.Black)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "¿No tienes una cuenta? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Regístrate",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 4.dp).clickable { onNavigateToRegister() }
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

@Preview(name = "Login Screen - Modo Claro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun LoginScreenLightPreview() {
    LioraTheme(darkTheme = false) {
        LoginScreen(
            uiState = AuthUiState(email = "usuario@liora.com"),
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClick = {},
            onNavigateToRegister = {}
        )
    }
}

@Preview(name = "Login Screen - Modo Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginScreenDarkPreview() {
    LioraTheme(darkTheme = true) {
        LoginScreen(
            uiState = AuthUiState(email = "usuario@liora.com"),
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClick = {},
            onNavigateToRegister = {}
        )
    }
}
