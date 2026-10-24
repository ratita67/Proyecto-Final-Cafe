package com.example.proyectito.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.proyectito.data.local.Pedido
import com.example.proyectito.data.repository.CafeRepository
import kotlinx.coroutines.flow.Flow

class PerfilViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = CafeRepository.obtener(app)

    fun pedidos(email: String): Flow<List<Pedido>> = repo.pedidosDe(email)
}
