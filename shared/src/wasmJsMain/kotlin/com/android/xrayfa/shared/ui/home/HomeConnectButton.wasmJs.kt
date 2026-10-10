package com.android.xrayfa.shared.ui.home

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

internal actual fun Modifier.connectButtonSurface(
    brush: Brush,
    shadowElevation: Dp,
    shadowColor: Color,
): Modifier = this.background(brush)
