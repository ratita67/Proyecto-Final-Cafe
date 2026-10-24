package com.example.proyectito.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos ORDER BY id")
    fun observarTodos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id")
    fun observarPorId(id: Int): Flow<Producto?>

    @Query("SELECT COUNT(*) FROM productos")
    suspend fun contar(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(lista: List<Producto>)
}

@Dao
interface CarritoDao {
    @Query(
        "SELECT p.id AS productoId, p.nombre AS nombre, p.precio AS precio, " +
            "p.emoji AS emoji, p.colorHex AS colorHex, c.cantidad AS cantidad " +
            "FROM carrito c INNER JOIN productos p ON p.id = c.productoId ORDER BY p.id"
    )
    fun observarDetalle(): Flow<List<CarritoDetalle>>

    @Query("SELECT COALESCE(SUM(cantidad), 0) FROM carrito")
    fun observarTotalItems(): Flow<Int>

    @Query("SELECT cantidad FROM carrito WHERE productoId = :id")
    suspend fun cantidadDe(id: Int): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(item: CarritoItem)

    @Query("DELETE FROM carrito WHERE productoId = :id")
    suspend fun eliminar(id: Int)

    @Query("DELETE FROM carrito")
    suspend fun vaciar()
}

@Dao
interface PedidoDao {
    @Insert
    suspend fun insertar(pedido: Pedido): Long

    @Query("SELECT * FROM pedidos WHERE userEmail = :email ORDER BY fecha DESC")
    fun observarDeUsuario(email: String): Flow<List<Pedido>>
}

@Dao
interface SucursalDao {
    @Query("SELECT * FROM sucursales ORDER BY id")
    fun observarTodas(): Flow<List<Sucursal>>

    @Query("SELECT COUNT(*) FROM sucursales")
    suspend fun contar(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(lista: List<Sucursal>)
}
