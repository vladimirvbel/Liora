package com.losindomables.liora

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.losindomables.liora.data.local.SessionManager
import kotlinx.coroutines.launch

// TODO(#14): ARCHIVO TEMPORAL. Eliminar junto con su uso en MainActivity al terminar de
//  verificar el criterio de aceptación (guardar / leer / borrar token sin perderlo al reabrir).
@Composable
fun TempSessionTest(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()

    val token by sessionManager.token.collectAsState(initial = null)
    val role by sessionManager.role.collectAsState(initial = null)

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Token guardado: ${token ?: "(ninguno)"}")
        Text("Rol guardado: ${role ?: "(ninguno)"}")

        Button(onClick = {
            scope.launch { sessionManager.saveSession("token-de-prueba-123", "CLIENTE") }
        }) { Text("Guardar token de prueba") }

        Button(onClick = {
            scope.launch { sessionManager.clearSession() }
        }) { Text("Borrar token") }
    }
}
