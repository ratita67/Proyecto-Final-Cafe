package com.example.proyectito.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectito.ui.screens.auth.LoginScreen
import com.example.proyectito.ui.screens.auth.RegisterScreen
import com.example.proyectito.ui.screens.carrito.CarritoScreen
import com.example.proyectito.ui.screens.carrito.CheckoutScreen
import com.example.proyectito.ui.screens.mapa.MapaScreen
import com.example.proyectito.ui.screens.menu.DetalleProductoScreen
import com.example.proyectito.ui.screens.menu.MenuScreen
import com.example.proyectito.ui.screens.perfil.PerfilScreen
import com.example.proyectito.ui.theme.Caramelo
import com.example.proyectito.ui.theme.Crema
import com.example.proyectito.ui.theme.Espresso
import com.example.proyectito.viewmodel.AuthViewModel
import com.example.proyectito.viewmodel.CarritoViewModel
import com.example.proyectito.viewmodel.MapaViewModel
import com.example.proyectito.viewmodel.MenuViewModel
import com.example.proyectito.viewmodel.PerfilViewModel

private data class ItemMenuInferior(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
)

private val itemsMenu = listOf(
    ItemMenuInferior(Rutas.MENU, "Menú", Icons.Filled.Coffee),
    ItemMenuInferior(Rutas.CARRITO, "Carrito", Icons.Filled.ShoppingCart),
    ItemMenuInferior(Rutas.MAPA, "Sucursales", Icons.Filled.Map),
    ItemMenuInferior(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // ViewModels compartidos (viven mientras viva la Activity)
    val authViewModel: AuthViewModel = viewModel()
    val menuViewModel: MenuViewModel = viewModel()
    val carritoViewModel: CarritoViewModel = viewModel()
    val mapaViewModel: MapaViewModel = viewModel()
    val perfilViewModel: PerfilViewModel = viewModel()

    val usuario by authViewModel.usuario.collectAsState()
    val totalItems by carritoViewModel.totalItems.collectAsState()

    val entrada by navController.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route

    // Se decide una sola vez al arrancar: si ya hay sesión, se salta el login
    val destinoInicial = remember { if (authViewModel.estaLogueado()) Rutas.MENU else Rutas.LOGIN }

    val nombre = usuario?.displayName?.takeIf { it.isNotBlank() }
        ?: usuario?.email?.substringBefore("@")
        ?: "amigo"
    val email = usuario?.email ?: ""

    fun irAPrincipal(ruta: String) {
        navController.navigate(ruta) {
            popUpTo(Rutas.MENU) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        containerColor = Crema,
        bottomBar = {
            if (rutaActual in Rutas.principales) {
                NavigationBar(containerColor = Color.White) {
                    itemsMenu.forEach { item ->
                        NavigationBarItem(
                            selected = rutaActual == item.ruta,
                            onClick = { irAPrincipal(item.ruta) },
                            icon = {
                                if (item.ruta == Rutas.CARRITO && totalItems > 0) {
                                    BadgedBox(badge = {
                                        Badge(containerColor = Caramelo) { Text(totalItems.toString()) }
                                    }) { Icon(item.icono, contentDescription = item.titulo) }
                                } else {
                                    Icon(item.icono, contentDescription = item.titulo)
                                }
                            },
                            label = { Text(item.titulo) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Espresso,
                                indicatorColor = Espresso,
                                unselectedIconColor = Espresso.copy(alpha = 0.6f),
                                unselectedTextColor = Espresso.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = destinoInicial,
            modifier = Modifier.padding(padding)
        ) {
            composable(Rutas.LOGIN) {
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginExitoso = {
                        navController.navigate(Rutas.MENU) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    },
                    onIrARegistro = { navController.navigate(Rutas.REGISTRO) }
                )
            }
            composable(Rutas.REGISTRO) {
                RegisterScreen(
                    authViewModel = authViewModel,
                    onRegistroExitoso = {
                        navController.navigate(Rutas.MENU) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    },
                    onVolverALogin = { navController.popBackStack() }
                )
            }

            // ---- 4 pantallas principales ----
            composable(Rutas.MENU) {
                MenuScreen(
                    menuViewModel = menuViewModel,
                    nombreUsuario = nombre,
                    onProductoClick = { id -> navController.navigate(Rutas.detalle(id)) }
                )
            }
            composable(Rutas.CARRITO) {
                CarritoScreen(
                    carritoViewModel = carritoViewModel,
                    onIrAlMenu = { irAPrincipal(Rutas.MENU) },
                    onContinuar = { navController.navigate(Rutas.CHECKOUT) }
                )
            }
            composable(Rutas.MAPA) {
                MapaScreen(mapaViewModel = mapaViewModel)
            }
            composable(Rutas.PERFIL) {
                PerfilScreen(
                    perfilViewModel = perfilViewModel,
                    nombre = nombre,
                    email = email,
                    onCerrarSesion = {
                        authViewModel.logout()
                        navController.navigate(Rutas.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // ---- 2 pantallas secundarias ----
            composable(
                route = Rutas.DETALLE,
                arguments = listOf(navArgument("productoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("productoId") ?: 0
                DetalleProductoScreen(
                    productoId = id,
                    menuViewModel = menuViewModel,
                    onVolver = { navController.popBackStack() }
                )
            }
            composable(Rutas.CHECKOUT) {
                CheckoutScreen(
                    carritoViewModel = carritoViewModel,
                    emailUsuario = email,
                    onVolver = { navController.popBackStack() },
                    onVerPedidos = {
                        navController.navigate(Rutas.PERFIL) {
                            popUpTo(Rutas.MENU)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

