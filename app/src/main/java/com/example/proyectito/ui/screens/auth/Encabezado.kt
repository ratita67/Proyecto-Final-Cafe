package com.example.proyectito.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.proyectito.ui.theme.CarameloClaro
import com.example.proyectito.ui.theme.Espresso
import com.example.proyectito.ui.theme.Mocha

/** Cabecera con degradado café, compartida por Login y Registro. */
@Composable
fun EncabezadoAuth(titulo: String, subtitulo: String, altura: Int = 250) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(altura.dp)
            .clip(RoundedCornerShape(bottomStart = 44.dp, bottomEnd = 44.dp))
            .background(Brush.verticalGradient(listOf(Espresso, Mocha))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("☕", fontSize = 56.sp)
            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitulo,
                style = MaterialTheme.typography.bodyMedium,
                color = CarameloClaro,
                textAlign = TextAlign.Center
            )
        }
    }
}
