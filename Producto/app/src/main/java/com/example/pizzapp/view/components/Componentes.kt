package com.example.pizzapp.view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pizzapp.view.core.navegation.Home

@Composable
fun BotonAnimado(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorFondo: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = RoundedCornerShape(12.dp),
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "scaleAnim"
    )

    val animatedColor by animateColorAsState(
        targetValue = if (isPressed) colorFondo.copy(alpha = 0.7f) else colorFondo,
        animationSpec = tween(durationMillis = 100),
        label = "colorAnim"
    )

    Button(
        onClick = onClick,
        modifier = modifier.scale(scale),
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedColor
        ),
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
fun BarraNavegacionInferior(
    controladorNavegacion: NavController,
    rutaActual: String?,
    isGuest: Boolean
) {
    NavigationBar {
        NavigationBarItem(
            selected = rutaActual == "home",
            onClick = { controladorNavegacion.navigate(Home(isGuest = isGuest)) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Inicio") }
        )
    }
}
