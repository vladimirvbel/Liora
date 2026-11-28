package com.losindomables.liora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.losindomables.liora.ui.navigation.AppNavigation
import com.losindomables.liora.ui.theme.LioraTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as LioraApplication).container

        setContent {
            LioraTheme {
                AppNavigation(
                    authRepository = appContainer.authRepository,
                    onAuthSuccess = {
                        // Próximamente (#15): Navegación a pantalla principal según el rol
                    }
                )
            }
        }
    }
}
