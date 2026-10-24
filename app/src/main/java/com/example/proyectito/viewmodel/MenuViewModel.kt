package com.example.proyectito.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectito.data.local.Producto
import com.example.proyectito.data.repository.CafeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MenuViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = CafeRepository.obtener(app)

    val categorias = listOf("Todos", "Café", "Frío", "Postres")

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda

    private val _categoria = MutableStateFlow("Todos")
    val categoria: StateFlow<String> = _categoria

    val productos: StateFlow<List<Producto>> =
        combine(repo.productos, _busqueda, _categoria) { lista, texto, cat ->
            lista.filter { p ->
                (cat == "Todos" || p.categoria == cat) &&
                    p.nombre.contains(texto.trim(), ignoreCase = true)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch { repo.sembrarDatos() }
    }

    fun cambiarBusqueda(texto: String) {
        _busqueda.value = texto
    }

    fun cambiarCategoria(cat: String) {
        _categoria.value = cat
    }

    fun producto(id: Int): Flow<Producto?> = repo.producto(id)

    fun agregarAlCarrito(productoId: Int, cantidad: Int = 1) {
        viewModelScope.launch { repo.agregarAlCarrito(productoId, cantidad) }
    }
}
