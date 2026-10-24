package com.example.proyectito.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Producto del menú de la cafetería. */
@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val emoji: String,
    val colorHex: Long
)

/** Producto agregado al carrito (una fila por producto). */
@Entity(tableName = "carrito")
data class CarritoItem(
    @PrimaryKey val productoId: Int,
    val cantidad: Int
)

/** Pedido confirmado por un usuario. */
@Entity(tableName = "pedidos")
data class Pedido(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,
    val fecha: Long,
    val total: Double,
    val resumen: String,
    val entrega: String,
    val pago: String,
    val notas: String = ""
)

/** Sucursal que se muestra en el mapa. */
@Entity(tableName = "sucursales")
data class Sucursal(
    @PrimaryKey val id: Int,
    val nombre: String,
    val direccion: String,
    val horario: String,
    val latitud: Double,
    val longitud: Double
)

/** Resultado del JOIN carrito + productos (no es una tabla). */
data class CarritoDetalle(
    val productoId: Int,
    val nombre: String,
    val precio: Double,
    val emoji: String,
    val colorHex: Long,
    val cantidad: Int
) {
    val subtotal: Double get() = precio * cantidad
}
