package com.losindomables.liora.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.isSystemInDarkTheme

/**
 * Campo de contraseña ajustado al diseño de Figma.
 */
@Composable
fun LioraPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Contraseña",
    placeholder: String = "••••••••••••",
    leadingIcon: @Composable (() -> Unit)? = null, // Aquí puedes pasar tu icono de candado
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {},
    enabled: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }
    
    val isDark = isSystemInDarkTheme()
    val containerColor = if (isDark) Color(0xFF333333) else Color(0xFFF7EBEB)
    val inputTextColor = if (isDark) Color.White else Color.Black

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = if (isDark) Color.White else Color(0xFF1E1D1D)) },
        leadingIcon = leadingIcon,
            isError = errorMessage != null,
            supportingText = errorMessage?.let {
                { Text(text = it, color = MaterialTheme.colorScheme.error) }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onAny = { onImeAction() }
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    // AQUÍ PUEDES COLOCAR TU ICONO DE OJO USANDO Icon(painterResource(id = R.drawable.tu_icono_ojo), contentDescription = null)
                    Text(
                        text = if (passwordVisible) "👁" else "🙈",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
                focusedTextColor = inputTextColor,
                unfocusedTextColor = inputTextColor
            ),
            modifier = modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(12.dp),
                    clip = false
                )
        )
}
