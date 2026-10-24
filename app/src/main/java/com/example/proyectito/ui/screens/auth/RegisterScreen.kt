package com.example.proyectito.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegistroExitoso: () -> Unit,
    onVolverALogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    val estado by authViewModel.estado.collectAsState()

    LaunchedEffect(Unit) { authViewModel.limpiarError() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        EncabezadoAuth(
            titulo = "Crea tu cuenta",
            subtitulo = "Únete y pide sin hacer fila",
            altura = 210
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CafeTextField(
                valor = nombre,
                alCambiar = { nombre = it },
                etiqueta = "Nombre",
                icono = Icons.Filled.Person
            )
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
                etiqueta = "Contraseña (mínimo 6 caracteres)",
                icono = Icons.Filled.Lock,
                esPassword = true,
                tipoTeclado = KeyboardType.Password
            )
            CafeTextField(
                valor = confirmar,
                alCambiar = { confirmar = it },
                etiqueta = "Confirmar contraseña",
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
                texto = "Registrarme",
                cargando = estado.cargando,
                onClick = {
                    authViewModel.registrar(nombre, email, password, confirmar, onRegistroExitoso)
                }
            )

            TextButton(onClick = onVolverALogin, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "¿Ya tienes cuenta? Inicia sesión",
                    color = Caramelo,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
