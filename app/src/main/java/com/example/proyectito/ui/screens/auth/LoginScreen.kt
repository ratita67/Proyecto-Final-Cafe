package com.example.proyectito.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.proyectito.ui.components.BotonPrimario
import com.example.proyectito.ui.components.CafeTextField
import com.example.proyectito.ui.theme.Caramelo
import com.example.proyectito.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginExitoso: () -> Unit,
    onIrARegistro: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val estado by authViewModel.estado.collectAsState()

    LaunchedEffect(Unit) { authViewModel.limpiarError() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        EncabezadoAuth(titulo = "Cafecito", subtitulo = "Tu café favorito, a un toque de distancia")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Bienvenido de vuelta", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Inicia sesión para ver el menú y hacer tus pedidos.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))

            CafeTextField(
                valor = email,
                alCambiar = { email = it },
                etiqueta = "Correo electrónico",
                icono = Icons.Filled.Email,
                tipoTeclado = KeyboardType.Email
            )
            CafeTextField(
                valor = password,
                alCambiar = { password = it },
                etiqueta = "Contraseña",
                icono = Icons.Filled.Lock,
                esPassword = true,
                tipoTeclado = KeyboardType.Password,
                imeAction = ImeAction.Done
            )

            estado.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            BotonPrimario(
                texto = "Iniciar sesión",
                cargando = estado.cargando,
                onClick = { authViewModel.login(email, password, onLoginExitoso) }
            )

            TextButton(onClick = onIrARegistro, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "¿No tienes cuenta? Regístrate",
                    color = Caramelo,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
