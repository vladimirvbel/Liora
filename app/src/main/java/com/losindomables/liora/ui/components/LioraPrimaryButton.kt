package com.losindomables.liora.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.isSystemInDarkTheme
import com.losindomables.liora.ui.theme.LioraRose

/**
 * Botón principal de Liora ajustado al diseño de Figma.
 */
@Composable
fun LioraPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    val isDark = isSystemInDarkTheme()
    val textColor = if (isDark) Color(0xFF1E1D1D) else Color.White
    
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LioraRose,
            contentColor = textColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false
            )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
        }
    }
}
