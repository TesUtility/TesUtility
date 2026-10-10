package com.android.xrayfa.shared.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.xrayfa.shared.ui.theme.XrayBrand

/**
 * Логотип TESVPN — буква T с диагональным разрезом.
 * Имя сохранено, чтобы не ломать вызовы в HomeConnectButton.
 */
@Composable
fun GuardCatMark(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    bodyTint: Color = Color.White, // белая T по умолчанию
    eyeTint: Color = XrayBrand.BlueDeep, // цвет «выреза» (линия через T)
    contentDescription: String? = "TESVPN",
) {
    Box(modifier = modifier.size(size)) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height

// ─── Буква T с диагональным разрезом ─────────
// Рисуем как одну фигуру через EvenOdd:
// 1. Внешний контур буквы T
// 2. Линия разреза (внутренний контур) — «вырезается»

            val tPath = Path().apply {
                fillType = PathFillType.EvenOdd

// ── Внешний контур буквы T ──
// Верхняя горизонтальная перекладина
                moveTo(w * 0.15f, h * 0.15f)
                lineTo(w * 0.85f, h * 0.15f)
                lineTo(w * 0.85f, h * 0.32f)
                lineTo(w * 0.58f, h * 0.32f) // отступ вниз к «ножке»
// Правая сторона ножки
                lineTo(w * 0.58f, h * 0.85f)
                lineTo(w * 0.42f, h * 0.85f)
// Левая сторона ножки
                lineTo(w * 0.42f, h * 0.32f)
                lineTo(w * 0.15f, h * 0.32f) // возврат к перекладине
                close()

// ── Диагональный разрез ──
// Узкая полоса, проходящая слева-сверху → справо-снизу
// Через «далеко за пределами» — обрежется естественно
                moveTo(w * 0.05f, h * 0.45f)
                lineTo(w * 0.95f, h * -0.05f) // уходит вверх-вправо
                lineTo(w * 1.05f, h * 0.05f)
                lineTo(w * 0.15f, h * 0.55f)
                close()
            }

            drawPath(
                path = tPath,
                color = bodyTint,
                style = Fill,
            )
        }
    }
}