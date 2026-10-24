package com.example.proyectito.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectito.data.local.CarritoDetalle
import com.example.proyectito.data.repository.CafeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CarritoViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = CafeRepository.obtener(app)

    val items: StateFlow<List<CarritoDetalle>> = repo.carrito
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subtotal: StateFlow<Double> = repo.carrito
        .map { lista -> lista.sumOf { it.subtotal } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalItems: StateFlow<Int> = repo.totalItemsCarrito
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun aumentar(item: CarritoDetalle) {
        viewModelScope.launch { repo.cambiarCantidad(item.productoId, item.cantidad + 1) }
    }

    fun disminuir(item: CarritoDetalle) {
        viewModelScope.launch { repo.cambiarCantidad(item.productoId, item.cantidad - 1) }
    }

    fun quitar(item: CarritoDetalle) {
        viewModelScope.launch { repo.quitarDelCarrito(item.productoId) }
    }

    fun vaciar() {
        viewModelScope.launch { repo.vaciarCarrito() }
    }

    fun confirmarPedido(
        email: String,
        entrega: String,
        pago: String,
        notas: String,
        costoEnvio: Double,
        onListo: () -> Unit
    ) {
        val lista = items.value
        if (lista.isEmpty()) return
        val total = lista.sumOf { it.subtotal } + costoEnvio
        viewModelScope.launch {
            repo.confirmarPedido(email, lista, total, entrega, pago, notas)
            onListo()
        }
    }
}
