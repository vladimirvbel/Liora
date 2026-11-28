package com.losindomables.liora.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.losindomables.liora.R

/**
 * Logotipo de Liora adaptativo al tema claro y oscuro.
 */
@Composable
fun LioraLogo(
    modifier: Modifier = Modifier,
    isDark: Boolean = isSystemInDarkTheme()
) {
    val logoRes = if (isDark) R.drawable.color_rosa_empolvado else R.drawable.color_negro
    Image(
        painter = painterResource(id = logoRes),
        contentDescription = "Liora",
        modifier = modifier.height(64.dp)
    )
}
