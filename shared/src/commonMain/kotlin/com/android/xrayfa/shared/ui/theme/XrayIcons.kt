package com.android.xrayfa.shared.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Единый набор иконок проекта.
 *
 * Правило: для навигации/топбаров используется [filled] (активное) + [outlined] (неактивное),
 * для пунктов меню и списков — только [outlined] (лёгкий современный вид).
 *
 * Все размеры берутся из [XrayDimens], а не задаются на месте.
 */
object XrayIcons {
    // ── Нижняя навигация (гибрид: активное filled / неактивное outlined) ──
    val HomeActive: ImageVector = Icons.Filled.PowerSettingsNew
    val HomeInactive: ImageVector = Icons.Outlined.PowerSettingsNew
    val SettingsActive: ImageVector = Icons.Filled.Settings
    val SettingsInactive: ImageVector = Icons.Outlined.Settings

    // ── Топбары и экшены ──
    val Settings: ImageVector = Icons.Outlined.Settings
    val Config: ImageVector = Icons.Outlined.Tune
    val Search: ImageVector = Icons.Outlined.Search
    val Speed: ImageVector = Icons.Outlined.Speed

    // ── Пункты меню ──
    val Star: ImageVector = Icons.Outlined.Star
    val StarActive: ImageVector = Icons.Filled.Star
    val BugReport: ImageVector = Icons.Outlined.BugReport
    val DeleteAll: ImageVector = Icons.Outlined.DeleteForever
}
