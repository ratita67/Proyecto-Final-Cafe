package com.example.proyectito.ui.screens.mapa

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.proyectito.data.local.Sucursal
import com.example.proyectito.ui.theme.Caramelo
import com.example.proyectito.ui.theme.Espresso
import com.example.proyectito.ui.theme.Latte
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.example.proyectito.viewmodel.MapaViewModel

@Composable
fun MapaScreen(mapaViewModel: MapaViewModel) {
    val sucursales by mapaViewModel.sucursales.collectAsState()
    val seleccionada by mapaViewModel.seleccionada.collectAsState()

    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(19.4000, -99.1700), 11.5f)
    }

    // Cuando se elige una sucursal, la cámara se acerca a ella
    LaunchedEffect(seleccionada, sucursales) {
        val destino = sucursales.firstOrNull { it.id == seleccionada } ?: return@LaunchedEffect
        camara.animate(
            CameraUpdateFactory.newLatLngZoom(LatLng(destino.latitud, destino.longitud), 15f)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = camara
        ) {
            sucursales.forEach { sucursal ->
                val estado = rememberMarkerState(
                    key = sucursal.id.toString(),
                    position = LatLng(sucursal.latitud, sucursal.longitud)
                )
                Marker(
                    state = estado,
                    title = sucursal.nombre,
                    snippet = sucursal.direccion,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                    onClick = {
                        mapaViewModel.seleccionar(sucursal.id)
                        false
                    }
                )
            }
        }

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Espresso),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            Text(
                "☕ Nuestras sucursales",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
            )
        }

        LazyRow(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sucursales, key = { it.id }) { sucursal ->
                TarjetaSucursal(
                    sucursal = sucursal,
                    activa = sucursal.id == seleccionada,
                    onClick = { mapaViewModel.seleccionar(sucursal.id) }
                )
            }
        }
    }
}

@Composable
private fun TarjetaSucursal(sucursal: Sucursal, activa: Boolean, onClick: () -> Unit) {
    val contexto = LocalContext.current
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(2.dp, if (activa) Caramelo else Latte),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.width(260.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(sucursal.nombre, style = MaterialTheme.typography.titleMedium)
            Text(
                sucursal.direccion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                sucursal.horario,
                style = MaterialTheme.typography.bodyMedium,
                color = Caramelo
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {
                    val uri = Uri.parse(
                        "geo:${sucursal.latitud},${sucursal.longitud}" +
                            "?q=${sucursal.latitud},${sucursal.longitud}(${Uri.encode(sucursal.nombre)})"
                    )
                    try {
                        contexto.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    } catch (e: Exception) {
                        Toast.makeText(contexto, "No hay una app de mapas disponible", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(Icons.Filled.Directions, contentDescription = null, tint = Caramelo)
                    Text(" Cómo llegar", color = Caramelo)
                }
            }
        }
    }
}
