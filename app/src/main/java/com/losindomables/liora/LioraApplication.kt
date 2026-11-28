package com.losindomables.liora

import android.app.Application
import com.losindomables.liora.di.AppContainer
import com.losindomables.liora.di.DefaultAppContainer

/**
 * Clase Application principal que inicializa el [AppContainer] manual.
 */
class LioraApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
