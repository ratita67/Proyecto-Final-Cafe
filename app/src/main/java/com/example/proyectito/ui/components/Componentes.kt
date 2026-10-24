package com.example.proyectito.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectito.ui.theme.Caramelo
import com.example.proyectito.ui.theme.Espresso
import com.example.proyectito.ui.theme.Latte
import com.example.proyectito.ui.theme.Mocha
import java.util.Locale

fun formatoPrecio(valor: Double): String = String.format(Locale.US, "$%.2f", valor)

/** Círculo con degradado y emoji que hace de "foto" del producto. */
@Composable
fun ProductoImagen(
    emoji: String,
    colorHex: Long,
    tamano: Dp,
    modifier: Modifier = Modifier
) {
    val base = Color(colorHex)
    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(Brush.radialGradient(listOf(base.copy(alpha = 0.45f), base))),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, fontSize = (tamano.value * 0.5f).sp)
    }
}

@Composable
fun BotonPrimario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    habilitado: Boolean = true,
    cargando: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = habilitado && !cargando,
        modifier = modifier.height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Espresso,
            contentColor = Color.White,
            disabledContainerColor = Mocha.copy(alpha = 0.5f),
            disabledContentColor = Color.White
        )
    ) {
        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = Color.White,
                strokeWidth = 2.5.dp
            )
        } else {
            Text(texto, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun CafeTextField(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    modifier: Modifier = Modifier.fillMaxWidth(),
    esPassword: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    unaLinea: Boolean = true
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        leadingIcon = { Icon(icono, contentDescription = null) },
        trailingIcon = if (esPassword) {
            {
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        imageVector = if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = "Mostrar u ocultar contraseña"
                    )
                }
            }
        } else null,
        visualTransformation = if (esPassword && !visible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado, imeAction = imeAction),
        singleLine = unaLinea,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Caramelo,
            focusedLabelColor = Mocha,
            cursorColor = Caramelo,
            focusedLeadingIconColor = Caramelo,
            unfocusedBorderColor = Mocha.copy(alpha = 0.35f),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        modifier = modifier
    )
}

@Composable
fun SelectorCantidad(
    cantidad: Int,
    onMenos: () -> Unit,
    onMas: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FilledIconButton(
            onClick = onMenos,
            modifier = Modifier.size(34.dp),
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = Latte, contentColor = Espresso)
        ) { Icon(Icons.Filled.Remove, contentDescription = "Quitar uno", modifier = Modifier.size(18.dp)) }

        Text(
            text = cantidad.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        FilledIconButton(
            onClick = onMas,
            modifier = Modifier.size(34.dp),
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = Caramelo, contentColor = Color.White)
        ) { Icon(Icons.Filled.Add, contentDescription = "Agregar uno", modifier = Modifier.size(18.dp)) }
    }
}
