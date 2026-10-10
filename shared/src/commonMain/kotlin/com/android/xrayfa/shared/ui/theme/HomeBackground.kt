package com.android.xrayfa.shared.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * Премиум-фон: градиент + радиальное свечение + микро-шум.
 * Оборачивает любой контент.
 */
@Composable
fun HomeBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val gradient = Brush.verticalGradient(
        0f to Color(0xFF0A0A0F),
        0.5f to Color(0xFF0F0F1A),
        1f to Color(0xFF0A0A0F),
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient),
    ) {
        // Radial purple glow behind the connect button (INCY style)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(420.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            XrayBrand.Blue.copy(alpha = 0.25f),
                            Color.Transparent,
                        )
                    )
                )
                .blur(48.dp),
        )

        content()

        // Subtle film grain overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .filmGrain(
                    alpha = 0.03f,
                    dotSize = 1f,
                    step = 4f,
                )
        )
    }
}

/**
 * Лёгкий процедурный шум.
 */
@Composable
private fun Modifier.filmGrain(
    alpha: Float = 0.05f,
    dotSize: Float = 1f,
    step: Float = 4f,
    seed: Int = 42,
): Modifier {
    val offsets = remember(seed) {
        val r = Random(seed)
        List(2048) { Offset(r.nextFloat(), r.nextFloat()) }
    }

    return this.drawWithContent {
        drawContent()

        val cols = (size.width / step).toInt()
        val rows = (size.height / step).toInt()
        var i = 0

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val jitter = offsets[i % offsets.size]
                i++
                drawCircle(
                    color = Color.White.copy(alpha = alpha),
                    radius = dotSize,
                    center = Offset(
                        x = col * step + jitter.x * step,
                        y = row * step + jitter.y * step,
                    ),
                    blendMode = BlendMode.Overlay,
                )
            }
        }
    }
}