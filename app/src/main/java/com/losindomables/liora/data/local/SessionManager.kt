package com.losindomables.liora.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Declarado a nivel de archivo: garantiza una única instancia de DataStore por archivo.
private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "liora_session"
)

/**
 * Persiste la sesión del usuario (token JWT y rol) en el dispositivo con Jetpack DataStore.
 *
 * Los datos se guardan en almacenamiento privado de la app y sobreviven al cierre del proceso.
 * Nota: DataStore Preferences no cifra los datos.
 */
class SessionManager(context: Context) {

    // applicationContext evita retener una Activity y provocar fugas de memoria.
    private val dataStore: DataStore<Preferences> = context.applicationContext.sessionDataStore

    /** Token JWT guardado, o `null` si no hay sesión. */
    val token: Flow<String?> = dataStore.safeData().map { it[KEY_TOKEN] }

    /** Rol guardado ("CLIENTE", "VENDEDOR" o "ADMIN"), o `null` si no hay sesión. */
    val role: Flow<String?> = dataStore.safeData().map { it[KEY_ROLE] }

    /** Guarda token y rol en una sola operación atómica. */
    suspend fun saveSession(token: String, role: String) {
        dataStore.edit { prefs ->
            prefs[KEY_TOKEN] = token
            prefs[KEY_ROLE] = role
        }
    }

    /** Elimina token y rol (cerrar sesión). */
    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }

    // Si falla la lectura del disco se emite un estado vacío en lugar de romper el Flow.
    private fun DataStore<Preferences>.safeData(): Flow<Preferences> =
        data.catch { e ->
            if (e is IOException) emit(emptyPreferences()) else throw e
        }

    private companion object {
        val KEY_TOKEN = stringPreferencesKey("auth_token")
        val KEY_ROLE = stringPreferencesKey("user_role")
    }
}
