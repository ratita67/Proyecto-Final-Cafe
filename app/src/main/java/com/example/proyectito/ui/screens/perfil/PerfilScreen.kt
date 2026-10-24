package com.example.proyectito.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectito.data.local.Pedido
import com.example.proyectito.ui.components.formatoPrecio
import com.example.proyectito.ui.theme.Caramelo
import com.example.proyectito.ui.theme.CarameloClaro
import com.example.proyectito.ui.theme.Espresso
import com.example.proyectito.ui.theme.Latte
import com.example.proyectito.ui.theme.Mocha
import com.example.proyectito.viewmodel.PerfilViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PerfilScreen(
    perfilViewModel: PerfilViewModel,
    nombre: String,
    email: String,
    onCerrarSesion: () -> Unit
) {
    val flujo = remember(email) { perfilViewModel.pedidos(email) }
    val pedidos by flujo.collectAsState(initial = emptyList())
    var confirmarSalida by remember { mutableStateOf(false) }
    val gastado = pedidos.sumOf { it.total }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(Espresso, Mocha)),
                        RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .background(Caramelo, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = nombre.firstOrNull()?.uppercase() ?: "☕",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Text(email, style = MaterialTheme.typography.bodyMedium, color = CarameloClaro)
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Estadistica("Pedidos", pedidos.size.toString(), Modifier.weight(1f))
                Estadistica("Gastado", formatoPrecio(gastado), Modifier.weight(1f))
            }
        }

        item {
            Text(
                "Mis pedidos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )
        }

        if (pedidos.isEmpty()) {
            item {
                Text(
                    "Aún no has hecho pedidos. ¡Anímate a probar algo del menú!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        } else {
            items(pedidos, key = { it.id }) { pedido ->
                TarjetaPedido(pedido)
            }
        }

        item {
            OutlinedButton(
                onClick = { confirmarSalida = true },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Text("  Cerrar sesión")
            }
        }
    }

    if (confirmarSalida) {
        AlertDialog(
            onDismissRequest = { confirmarSalida = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Seguro que quieres salir de tu cuenta?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarSalida = false
                    onCerrarSesion()
                }) { Text("Salir", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmarSalida = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun Estadistica(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Latte)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(valor, style = MaterialTheme.typography.titleLarge, color = Espresso)
            Text(titulo, style = MaterialTheme.typography.bodyMedium, color = Mocha)
        }
    }
}

@Composable
private fun TarjetaPedido(pedido: Pedido) {
    val fecha = remember(pedido.fecha) {
        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("es", "MX")).format(Date(pedido.fecha))
    }
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pedido #${pedido.id}", style = MaterialTheme.typography.titleMedium)
                Text(
                    formatoPrecio(pedido.total),
                    style = MaterialTheme.typography.titleMedium,
                    color = Caramelo
                )
            }
            Text(fecha, style = MaterialTheme.typography.bodyMedium, color = Mocha)
            Text(pedido.resumen, style = MaterialTheme.typography.bodyMedium)
            Text(
                "${pedido.entrega} · ${pedido.pago}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (pedido.notas.isNotBlank()) {
                Text(
                    "Nota: ${pedido.notas}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Mocha
                )
            }
        }
    }
}
