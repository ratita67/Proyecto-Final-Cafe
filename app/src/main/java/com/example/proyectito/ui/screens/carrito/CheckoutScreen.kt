package com.example.proyectito.ui.screens.carrito

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyectito.ui.components.BotonPrimario
import com.example.proyectito.ui.components.CafeTextField
import com.example.proyectito.ui.components.formatoPrecio
import com.example.proyectito.ui.theme.Caramelo
import com.example.proyectito.ui.theme.Espresso
import com.example.proyectito.ui.theme.Latte
import com.example.proyectito.viewmodel.CarritoViewModel

private const val COSTO_ENVIO = 25.0

/** Pantalla secundaria: se llega desde el Carrito. */
@Composable
fun CheckoutScreen(
    carritoViewModel: CarritoViewModel,
    emailUsuario: String,
    onVolver: () -> Unit,
    onVerPedidos: () -> Unit
) {
    val items by carritoViewModel.items.collectAsState()
    val subtotal by carritoViewModel.subtotal.collectAsState()

    var entrega by remember { mutableStateOf("Recoger en sucursal") }
    var pago by remember { mutableStateOf("Efectivo") }
    var notas by remember { mutableStateOf("") }
    var confirmado by remember { mutableStateOf(false) }

    val envio = if (entrega == "A domicilio") COSTO_ENVIO else 0.0
    val total = subtotal + envio

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Confirmar pedido", style = MaterialTheme.typography.headlineMedium)
        }

        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Resumen
            TarjetaSeccion(titulo = "Tu pedido") {
                items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${item.cantidad}x ${item.nombre}", style = MaterialTheme.typography.bodyLarge)
                        Text(formatoPrecio(item.subtotal), style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            // Entrega
            TarjetaSeccion(titulo = "Entrega") {
                OpcionRadio("Recoger en sucursal", "Gratis", entrega == "Recoger en sucursal") {
                    entrega = "Recoger en sucursal"
                }
                OpcionRadio("A domicilio", formatoPrecio(COSTO_ENVIO), entrega == "A domicilio") {
                    entrega = "A domicilio"
                }
            }

            // Pago
            TarjetaSeccion(titulo = "Método de pago") {
                OpcionRadio("Efectivo", "", pago == "Efectivo") { pago = "Efectivo" }
                OpcionRadio("Tarjeta", "", pago == "Tarjeta") { pago = "Tarjeta" }
            }

            CafeTextField(
                valor = notas,
                alCambiar = { notas = it },
                etiqueta = "Notas para tu pedido (opcional)",
                icono = Icons.Filled.EditNote
            )

            // Totales
            TarjetaSeccion(titulo = "Total") {
                FilaTotal("Subtotal", formatoPrecio(subtotal))
                FilaTotal("Envío", formatoPrecio(envio))
                HorizontalDivider(color = Latte)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", style = MaterialTheme.typography.titleLarge)
                    Text(formatoPrecio(total), style = MaterialTheme.typography.titleLarge, color = Caramelo)
                }
            }

            BotonPrimario(
                texto = "Confirmar pedido",
                habilitado = items.isNotEmpty() && !confirmado,
                onClick = {
                    confirmado = true
                    carritoViewModel.confirmarPedido(
                        email = emailUsuario,
                        entrega = entrega,
                        pago = pago,
                        notas = notas,
                        costoEnvio = envio,
                        onListo = {}
                    )
                }
            )
            Spacer(Modifier.height(12.dp))
        }
    }

    if (confirmado) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("¡Pedido confirmado! ☕") },
            text = { Text("Estamos preparando tu pedido. Puedes revisarlo en tu perfil.") },
            confirmButton = {
                TextButton(onClick = onVerPedidos) { Text("Ver mis pedidos", color = Caramelo) }
            }
        )
    }
}

@Composable
private fun TarjetaSeccion(titulo: String, contenido: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Latte),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, color = Espresso)
            contenido()
        }
    }
}

@Composable
private fun OpcionRadio(texto: String, detalle: String, seleccionado: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = seleccionado,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = Caramelo)
        )
        Text(texto, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        if (detalle.isNotEmpty()) {
            Text(detalle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun FilaTotal(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(etiqueta, style = MaterialTheme.typography.bodyLarge)
        Text(valor, style = MaterialTheme.typography.bodyLarge)
    }
}
