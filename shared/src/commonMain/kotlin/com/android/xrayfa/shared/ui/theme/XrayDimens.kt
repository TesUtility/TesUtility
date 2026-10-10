package com.android.xrayfa.shared.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Единая шкала размеров и отступов.
 *
 * Используется везде вместо магических чисел, чтобы меню/иконки/списки
 * выглядели выровненными.
 */
object XrayDimens {
    // ── Иконки ──
    /** Мелкая иконка (подпись, info-хинт). */
    val IconXs = 14.dp
    /** Иконка внутри цветного кружка пункта меню. */
    val IconSm = 18.dp
    /** Стандартная иконка списка/топбара. */
    val IconMd = 24.dp
    /** Иконка-триггер меню / нижней навигации. */
    val IconLg = 26.dp

    // ── Контейнеры иконок ──
    /** Цветной кружок под иконку в пункте меню. */
    val IconBadge = 32.dp
    /** Кнопка-триггер меню. */
    val IconButton = 40.dp

    // ── Отступы экрана ──
    /** Горизонтальный отступ контента от края экрана. */
    val ScreenHorizontal = 16.dp
    /** Отступ заголовка группы настроек (выровнен с карточкой). */
    val GroupTitleHorizontal = 24.dp
    /** Горизонтальный отступ карточки в списке. */
    val CardHorizontal = 12.dp

    // ── Вертикальные отступы ──
    val SpaceXs = 4.dp
    val SpaceSm = 8.dp
    val SpaceMd = 16.dp
    val SpaceLg = 24.dp

    // ── Формы ──
    val CornerSm = 12.dp
    val CornerMd = 20.dp
    val CornerLg = 32.dp
}
