package com.example.proyectito.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectito.data.local.Sucursal
import com.example.proyectito.data.repository.CafeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = CafeRepository.obtener(app)

    val sucursales: StateFlow<List<Sucursal>> = repo.sucursales
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _seleccionada = MutableStateFlow<Int?>(null)
    val seleccionada: StateFlow<Int?> = _seleccionada

    init {
        viewModelScope.launch { repo.sembrarDatos() }
    }

    fun seleccionar(id: Int) {
        _seleccionada.value = id
    }
}
