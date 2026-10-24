package com.example.proyectito.data.repository

import android.content.Context
import com.example.proyectito.data.local.AppDatabase
import com.example.proyectito.data.local.CarritoDetalle
import com.example.proyectito.data.local.CarritoItem
import com.example.proyectito.data.local.Pedido
import com.example.proyectito.data.local.Producto
import com.example.proyectito.data.local.Sucursal
import kotlinx.coroutines.flow.Flow

/** Única fuente de datos para los ViewModels (patrón Repository de MVVM). */
class CafeRepository private constructor(db: AppDatabase) {

    private val productoDao = db.productoDao()
    private val carritoDao = db.carritoDao()
    private val pedidoDao = db.pedidoDao()
    private val sucursalDao = db.sucursalDao()

    val productos: Flow<List<Producto>> = productoDao.observarTodos()
    val sucursales: Flow<List<Sucursal>> = sucursalDao.observarTodas()
    val carrito: Flow<List<CarritoDetalle>> = carritoDao.observarDetalle()
    val totalItemsCarrito: Flow<Int> = carritoDao.observarTotalItems()

    fun producto(id: Int): Flow<Producto?> = productoDao.observarPorId(id)

    fun pedidosDe(email: String): Flow<List<Pedido>> = pedidoDao.observarDeUsuario(email)

    /** Inserta los datos de ejemplo si las tablas están vacías. */
    suspend fun sembrarDatos() {
        if (productoDao.contar() == 0) productoDao.insertar(DatosIniciales.productos)
        if (sucursalDao.contar() == 0) sucursalDao.insertar(DatosIniciales.sucursales)
    }

    suspend fun agregarAlCarrito(productoId: Int, cantidad: Int) {
        val actual = carritoDao.cantidadDe(productoId) ?: 0
        carritoDao.guardar(CarritoItem(productoId, actual + cantidad))
    }

    suspend fun cambiarCantidad(productoId: Int, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) carritoDao.eliminar(productoId)
        else carritoDao.guardar(CarritoItem(productoId, nuevaCantidad))
    }

    suspend fun quitarDelCarrito(productoId: Int) = carritoDao.eliminar(productoId)

    suspend fun vaciarCarrito() = carritoDao.vaciar()

    /** Guarda el pedido con el contenido actual del carrito y lo vacía. */
    suspend fun confirmarPedido(
        email: String,
        items: List<CarritoDetalle>,
        total: Double,
        entrega: String,
        pago: String,
        notas: String
    ) {
        val resumen = items.joinToString(", ") { "${it.cantidad}x ${it.nombre}" }
        pedidoDao.insertar(
            Pedido(
                userEmail = email,
                fecha = System.currentTimeMillis(),
                total = total,
                resumen = resumen,
                entrega = entrega,
                pago = pago,
                notas = notas.trim()
            )
        )
        carritoDao.vaciar()
    }

    companion object {
        @Volatile
        private var instancia: CafeRepository? = null

        fun obtener(context: Context): CafeRepository =
            instancia ?: synchronized(this) {
                instancia ?: CafeRepository(AppDatabase.obtener(context)).also { instancia = it }
            }
    }
}
