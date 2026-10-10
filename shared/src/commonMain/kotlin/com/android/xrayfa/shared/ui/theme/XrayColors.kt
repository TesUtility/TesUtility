package com.android.xrayfa.shared.ui.theme

import androidx.compose.ui.graphics.Color

internal val PrimaryLight = Color(0xFF7C3AED) // было 0xFF0061A4
internal val OnPrimaryLight = Color(0xFFFFFFFF)
internal val PrimaryContainerLight = Color(0xFFE9D5FF) // было 0xFFD1E4FF
internal val OnPrimaryContainerLight = Color(0xFF2E1065) // было 0xFF001D36

internal val PrimaryDark = Color(0xFFC4B5FD) // было 0xFF9ECAFF
internal val OnPrimaryDark = Color(0xFF3B0764) // было 0xFF003258
internal val PrimaryContainerDark = Color(0xFF6D28D9) // было 0xFF00497D
internal val OnPrimaryContainerDark = Color(0xFFEDE9FE) // было 0xFFD1E4FF

internal val SecondaryLight = Color(0xFF535F70)
internal val OnSecondaryLight = Color(0xFFFFFFFF)
internal val SecondaryContainerLight = Color(0xFFD7E3F7)
internal val OnSecondaryContainerLight = Color(0xFF101C2B)

internal val SecondaryDark = Color(0xFFBBC7DB)
internal val OnSecondaryDark = Color(0xFF253140)
internal val SecondaryContainerDark = Color(0xFF3B4858)
internal val OnSecondaryContainerDark = Color(0xFFD7E3F7)

internal val TertiaryLight = Color(0xFF6B5778)
internal val OnTertiaryLight = Color(0xFFFFFFFF)
internal val TertiaryContainerLight = Color(0xFFF2DAFF)
internal val OnTertiaryContainerLight = Color(0xFF251431)

internal val TertiaryDark = Color(0xFFD7BEE4)
internal val OnTertiaryDark = Color(0xFF3B2948)
internal val TertiaryContainerDark = Color(0xFF523F5F)
internal val OnTertiaryContainerDark = Color(0xFFF2DAFF)

// Neutral surface ladder: dark tones (N-4/6/10/12/17/22/24)
internal val SurfaceLight = Color(0xFF0A0A0F)
internal val OnSurfaceLight = Color(0xFFE2E2E9)
internal val SurfaceVariantLight = Color(0xFF1A1A2E)
internal val OnSurfaceVariantLight = Color(0xFF9CA3AF)
internal val OutlineLight = Color(0xFF2A2A3E)
internal val OutlineVariantLight = Color(0xFF1A1A2E)
internal val SurfaceDimLight = Color(0xFF0A0A0F)
internal val SurfaceBrightLight = Color(0xFF37393E)
internal val SurfaceContainerLowestLight = Color(0xFF05050A)
internal val SurfaceContainerLowLight = Color(0xFF1A1A2E)
internal val SurfaceContainerLight = Color(0xFF16162A)
internal val SurfaceContainerHighLight = Color(0xFF222240)
internal val SurfaceContainerHighestLight = Color(0xFF2E2E50)

internal val SurfaceDark = Color(0xFF0A0A0F)
internal val OnSurfaceDark = Color(0xFFE2E2E9)
internal val SurfaceVariantDark = Color(0xFF1A1A2E)
internal val OnSurfaceVariantDark = Color(0xFF9CA3AF)
internal val OutlineDark = Color(0xFF2A2A3E)
internal val OutlineVariantDark = Color(0xFF1A1A2E)
internal val SurfaceDimDark = Color(0xFF0A0A0F)
internal val SurfaceBrightDark = Color(0xFF37393E)
internal val SurfaceContainerLowestDark = Color(0xFF05050A)
internal val SurfaceContainerLowDark = Color(0xFF1A1A2E)
internal val SurfaceContainerDark = Color(0xFF16162A)
internal val SurfaceContainerHighDark = Color(0xFF222240)
internal val SurfaceContainerHighestDark = Color(0xFF2E2E50)

internal val ErrorLight = Color(0xFFBA1A1A)
internal val OnErrorLight = Color(0xFFFFFFFF)
internal val ErrorDark = Color(0xFFFFB4AB)
internal val OnErrorDark = Color(0xFF690005)

/** Guard Cat launcher palette — stays brand-blue even when dynamic color is on. */
object XrayBrand {
    // ── INCY-совместимая фиолетовая палитра ──
    val BlueLight = Color(0xFF818CF8) // светло-фиолетовый (верх градиента) — INCY accent light
    val Blue = Color(0xFF6366F1) // основной фиолетовый — INCY primary
    val BlueDeep = Color(0xFF4F46E5) // тёмно-фиолетовый (низ градиента) — INCY accent deep

    // ── Фоновые цвета (INCY dark) ──
    val BgDark = Color(0xFF0A0A0F) // глубокий чёрный (как у INCY)
    val BgLight = Color(0xFF0A0A0F) // тёмный фон
    val SurfaceDark = Color(0xFF0A0A0F) // тёмный фон для диалогов/топбара

    // ── Вторичный фон (карточки, навигация) ──
    val SurfaceSecondary = Color(0xFF1A1A2E) // тёмно-серый для карточек
    val SurfaceSecondaryLight = Color(0xFF1A1A2E)

    // ── Свечение ──
    val Glow = Color(0xFF818CF8)

    // ── Главная кнопка (INCY-style контур) ──
    /** Тёмный диск вокруг кнопки питания. */
    val DiscFill = Color(0xFF2A2D3E)
    /** Светлая обводка внутреннего круга и глифа питания. */
    val RingOutline = Color(0xFFC7C9D9)

    // ── Текст ──
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFF9CA3AF)

    // ── Совместимость со старым кодом (Cat* теперь фиолетовые/светлые) ──
    val CatEyeAmber = Color(0xFFEDE9FE) // было жёлтое → светлое
    val CatEyeGreen = Color(0xFFC4B5FD) // было зелёное → светло-фиолетовое
}
