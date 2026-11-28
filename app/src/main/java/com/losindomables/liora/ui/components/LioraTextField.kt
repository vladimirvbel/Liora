package com.losindomables.liora.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.isSystemInDarkTheme

/**
 * Campo de texto reutilizable ajustado al diseño de Figma.
 */
@Composable
fun LioraTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null, // Aquí puedes pasar tu icono, ej: { Icon(painterResource(id = R.drawable.ic_email), ...) }
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
    enabled: Boolean = true
) {
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
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onAny = { onImeAction() }
            ),
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
