package com.losindomables.liora.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

/**
 * Persiste la sesión del usuario (token JWT y rol) de manera ENCRIPTADA.
 *
 * Se utiliza EncryptedSharedPreferences (Jetpack Security) respaldado por el
 * Android Keystore. Los datos en el dispositivo no pueden ser leídos en texto plano.
 */
class SessionManagerImpl(context: Context) : SessionManager {

    private val masterKey = MasterKey.Builder(context.applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context.applicationContext,
        "liora_secure_session",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override val token: Flow<String?> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == KEY_TOKEN) {
                trySend(prefs.getString(KEY_TOKEN, null))
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        // Emitir el valor inicial
        trySend(sharedPreferences.getString(KEY_TOKEN, null))
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override val role: Flow<String?> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == KEY_ROLE) {
                trySend(prefs.getString(KEY_ROLE, null))
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        trySend(sharedPreferences.getString(KEY_ROLE, null))
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override suspend fun saveSession(token: String, role: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_ROLE, role)
                .apply()
        }
    }

    override suspend fun clearSession() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().clear().apply()
        }
    }

    private companion object {
        const val KEY_TOKEN = "auth_token"
        const val KEY_ROLE = "user_role"
    }
}