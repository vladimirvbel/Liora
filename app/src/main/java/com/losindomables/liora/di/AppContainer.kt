package com.losindomables.liora.di

import android.content.Context
import com.losindomables.liora.data.local.SessionManager
import com.losindomables.liora.data.local.SessionManagerImpl
import com.losindomables.liora.data.remote.RetrofitClient
import com.losindomables.liora.data.repository.AuthRepositoryImpl
import com.losindomables.liora.domain.repository.AuthRepository

/**
 * Contenedor manual de inyección de dependencias (DIP - SOLID).
 */
interface AppContainer {
    val sessionManager: SessionManager
    val authRepository: AuthRepository
}

/**
 * Implementación por defecto que instancia dependencias reales de la app.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    override val sessionManager: SessionManager by lazy {
        SessionManagerImpl(context.applicationContext)
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            authApi = RetrofitClient.authApi,
            sessionManager = sessionManager
        )
    }
}
