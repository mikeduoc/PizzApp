package com.example.pizzapp.view.pizzapp

import androidx.compose.animation.core.animate
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import com.example.pizzapp.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun PantallaInicio(
    onUnlocked: () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenHeight = constraints.maxHeight
        val coroutineScope = rememberCoroutineScope()
        var offsetY by remember { mutableStateOf(0f) }

        Image(
            painter = painterResource(id = R.drawable.inicio),
            contentDescription = "Pantalla de bloqueo",
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(0, offsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            val newOffsetY = offsetY + dragAmount
                            // Allow dragging up only
                            if (newOffsetY < 0) {
                                offsetY = newOffsetY
                            }
                        },
                        onDragEnd = {
                            coroutineScope.launch {
                                // If swiped more than a third of the screen, consider it unlocked
                                if (offsetY < -screenHeight / 3) {
                                    animate(
                                        initialValue = offsetY,
                                        targetValue = -screenHeight.toFloat(),
                                        block = { value, _ ->
                                            offsetY = value
                                        }
                                    )
                                    onUnlocked()
                                } else {
                                    // Otherwise, snap back to original position
                                    animate(
                                        initialValue = offsetY,
                                        targetValue = 0f,
                                        block = { value, _ ->
                                            offsetY = value
                                        }
                                    )
                                }
                            }
                        }
                    )
                },
            contentScale = ContentScale.Crop
        )
    }
}
