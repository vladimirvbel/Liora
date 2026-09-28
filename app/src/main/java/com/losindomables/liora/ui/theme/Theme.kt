package com.losindomables.liora.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = LioraPurple,
    secondary = LioraTeal,
    tertiary = BrightAqua,
    background = DarkNavy, // El azul oscuro es perfecto para el fondo en modo oscuro
    surface = DarkNavy,
    onPrimary = White,
    onSecondary = DarkNavy,
    onBackground = LightLilac, // El lila claro sirve muy bien para textos sobre el fondo oscuro
    onSurface = LightLilac
)

private val LightColorScheme = lightColorScheme(
    primary = LioraPurple,
    secondary = LioraTeal,
    tertiary = BrightAqua,
    background = LightLilac, // Un fondo muy suave para el modo claro
    surface = White,
    onPrimary = White,
    onSecondary = DarkNavy,
    onBackground = DarkNavy,
    onSurface = DarkNavy
)

@Composable
fun LioraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}