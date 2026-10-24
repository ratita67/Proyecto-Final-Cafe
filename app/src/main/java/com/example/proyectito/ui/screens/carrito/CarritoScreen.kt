package com.example.proyectito.ui.screens.carrito

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectito.data.local.CarritoDetalle
import com.example.proyectito.ui.components.BotonPrimario
import com.example.proyectito.ui.components.ProductoImagen
import com.example.proyectito.ui.components.SelectorCantidad
import com.example.proyectito.ui.components.formatoPrecio
import com.example.proyectito.ui.theme.Caramelo
import com.example.proyectito.ui.theme.Espresso
import com.example.proyectito.viewmodel.CarritoViewModel

@Composable
fun CarritoScreen(
    carritoViewModel: CarritoViewModel,
    onIrAlMenu: () -> Unit,
    onContinuar: () -> Unit
) {
    val items by carritoViewModel.items.collectAsState()
    val subtotal by carritoViewModel.subtotal.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 12.dp, top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mi carrito", style = MaterialTheme.typography.headlineMedium)
            if (items.isNotEmpty()) {
                TextButton(onClick = carritoViewModel::vaciar) {
                    Text("Vaciar", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        if (items.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🛒", fontSize = 72.sp)
                Text(
                    "Tu carrito está vacío",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Text(
                    "Agrega algo rico del menú para empezar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                )
                BotonPrimario(
                    texto = "Ver el menú",
                    onClick = onIrAlMenu,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items, key = { it.productoId }) { item ->
                    ItemCarrito(
                        item = item,
                        onMenos = { carritoViewModel.disminuir(item) },
                        onMas = { carritoViewModel.aumentar(item) },
                        onQuitar = { carritoViewModel.quitar(item) }
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", style = MaterialTheme.typography.titleMedium)
                        Text(
                            formatoPrecio(subtotal),
                            style = MaterialTheme.typography.titleLarge,
                            color = Caramelo
                        )
                    }
                    BotonPrimario(texto = "Continuar al pago", onClick = onContinuar)
                }
            }
        }
    }
}

@Composable
private fun ItemCarrito(
    item: CarritoDetalle,
    onMenos: () -> Unit,
    onMas: () -> Unit,
    onQuitar: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductoImagen(emoji = item.emoji, colorHex = item.colorHex, tamano = 64.dp)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(item.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    formatoPrecio(item.subtotal),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Caramelo
                )
                SelectorCantidad(cantidad = item.cantidad, onMenos = onMenos, onMas = onMas)
            }
            IconButton(onClick = onQuitar) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Quitar del carrito",
                    tint = Espresso.copy(alpha = 0.6f)
                )
            }
        }
    }
}
