package com.example.proyectito.navigation

object Rutas {
    // Autenticación
    const val LOGIN = "login"
    const val REGISTRO = "registro"

    // Pantallas principales (menú inferior)
    const val MENU = "menu"
    const val CARRITO = "carrito"
    const val MAPA = "mapa"
    const val PERFIL = "perfil"

    // Pantallas secundarias
    const val DETALLE = "detalle/{productoId}"   // Menú -> Detalle
    const val CHECKOUT = "checkout"              // Carrito -> Checkout

    fun detalle(productoId: Int) = "detalle/$productoId"

    val principales = setOf(MENU, CARRITO, MAPA, PERFIL)
}
