package com.example.proyectito.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AuthUiState(
    val cargando: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _usuario = MutableStateFlow(auth.currentUser)
    val usuario: StateFlow<FirebaseUser?> = _usuario.asStateFlow()

    private val _estado = MutableStateFlow(AuthUiState())
    val estado: StateFlow<AuthUiState> = _estado.asStateFlow()

    fun estaLogueado(): Boolean = auth.currentUser != null

    fun limpiarError() {
        _estado.value = _estado.value.copy(error = null)
    }

    fun login(email: String, password: String, onExito: () -> Unit) {
        val correo = email.trim()
        if (correo.isEmpty() || password.isEmpty()) {
            _estado.value = AuthUiState(error = "Completa todos los campos")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            _estado.value = AuthUiState(error = "El correo no es válido")
            return
        }
        _estado.value = AuthUiState(cargando = true)
        auth.signInWithEmailAndPassword(correo, password).addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                _usuario.value = auth.currentUser
                _estado.value = AuthUiState()
                onExito()
            } else {
                _estado.value = AuthUiState(error = traducirError(tarea.exception))
            }
        }
    }

    fun registrar(
        nombre: String,
        email: String,
        password: String,
        confirmar: String,
        onExito: () -> Unit
    ) {
        val correo = email.trim()
        val nombreLimpio = nombre.trim()
        when {
            nombreLimpio.isEmpty() || correo.isEmpty() || password.isEmpty() || confirmar.isEmpty() ->
                _estado.value = AuthUiState(error = "Completa todos los campos")
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() ->
                _estado.value = AuthUiState(error = "El correo no es válido")
            password.length < 6 ->
                _estado.value = AuthUiState(error = "La contraseña debe tener al menos 6 caracteres")
            password != confirmar ->
                _estado.value = AuthUiState(error = "Las contraseñas no coinciden")
            else -> {
                _estado.value = AuthUiState(cargando = true)
                auth.createUserWithEmailAndPassword(correo, password).addOnCompleteListener { tarea ->
                    if (tarea.isSuccessful) {
                        val cambios = UserProfileChangeRequest.Builder()
                            .setDisplayName(nombreLimpio)
                            .build()
                        auth.currentUser?.updateProfile(cambios)?.addOnCompleteListener {
                            _usuario.value = auth.currentUser
                            _estado.value = AuthUiState()
                            onExito()
                        } ?: run {
                            _estado.value = AuthUiState()
                            onExito()
                        }
                    } else {
                        _estado.value = AuthUiState(error = traducirError(tarea.exception))
                    }
                }
            }
        }
    }

    fun logout() {
        auth.signOut()
        _usuario.value = null
        _estado.value = AuthUiState()
    }

    private fun traducirError(e: Exception?): String = when (e) {
        is FirebaseAuthInvalidUserException -> "No existe una cuenta con ese correo"
        is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos"
        is FirebaseAuthUserCollisionException -> "Ese correo ya está registrado"
        else -> "No se pudo completar la operación. Revisa tu conexión e inténtalo de nuevo"
    }
}
