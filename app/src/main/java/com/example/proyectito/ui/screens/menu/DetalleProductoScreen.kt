package com.example.proyectito.ui.screens.menu

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectito.ui.components.BotonPrimario
import com.example.proyectito.ui.components.SelectorCantidad
import com.example.proyectito.ui.components.formatoPrecio
import com.example.proyectito.ui.theme.Caramelo
import com.example.proyectito.viewmodel.MenuViewModel

/** Pantalla secundaria: se llega desde el Menú. */
@Composable
fun DetalleProductoScreen(
    productoId: Int,
    menuViewModel: MenuViewModel,
    onVolver: () -> Unit
) {
    val flujo = remember(productoId) { menuViewModel.producto(productoId) }
    val producto by flujo.collectAsState(initial = null)
    var cantidad by remember { mutableIntStateOf(1) }
    val contexto = LocalContext.current

    val p = producto
    if (p == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Cargando...")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(p.colorHex).copy(alpha = 0.55f), Color(p.colorHex))
                    ),
                    RoundedCornerShape(bottomStart = 44.dp, bottomEnd = 44.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = p.emoji, fontSize = 120.sp)
            IconButton(
                onClick = onVolver,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .background(Color.White.copy(alpha = 0.85f), RoundedCornerShape(50))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
        }

        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = p.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formatoPrecio(p.precio),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Caramelo
                )
            }
            Text(
                text = p.categoria.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = p.descripcion,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 6.dp)
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Cantidad", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                SelectorCantidad(
                    cantidad = cantidad,
                    onMenos = { if (cantidad > 1) cantidad-- },
                    onMas = { if (cantidad < 20) cantidad++ }
                )
            }

            Spacer(Modifier.height(10.dp))

            BotonPrimario(
                texto = "Agregar al carrito · ${formatoPrecio(p.precio * cantidad)}",
                onClick = {
                    menuViewModel.agregarAlCarrito(p.id, cantidad)
                    Toast.makeText(contexto, "Agregado al carrito", Toast.LENGTH_SHORT).show()
                    onVolver()
                }
            )
        }
    }
}
