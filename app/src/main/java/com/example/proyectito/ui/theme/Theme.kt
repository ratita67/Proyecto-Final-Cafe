package com.example.proyectito.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val EsquemaCafe = lightColorScheme(
    primary = Espresso,
    onPrimary = Crema,
    primaryContainer = Latte,
    onPrimaryContainer = Espresso,
    secondary = Caramelo,
    onSecondary = Superficie,
    secondaryContainer = CarameloClaro,
    onSecondaryContainer = Espresso,
    tertiary = Matcha,
    background = Crema,
    onBackground = Espresso,
    surface = Superficie,
    onSurface = Espresso,
    surfaceVariant = Latte,
    onSurfaceVariant = Mocha,
    outline = Mocha,
    error = ErrorRojo
)

private val Formas = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(26.dp)
)

@Composable
fun ProyectitoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaCafe,
        typography = Typography,
        shapes = Formas,
        content = content
    )
}
