package com.android.xrayfa.shared.ui.nav


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected as semanticsSelected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.xrayfa.shared.navigation.RootTab
import com.android.xrayfa.shared.ui.theme.XrayBrand
import com.android.xrayfa.shared.ui.theme.XrayDimens
import com.android.xrayfa.shared.ui.theme.XrayIcons
import kotlin.math.roundToInt

val FloatingNavBarHeight = 64.dp
val FloatingNavBottomMargin = 8.dp

/** Extra space so the last Config row can rest above the pill. */
val FloatingNavExtraContentPadding = 24.dp

private val BarCorner = 32.dp
private val IndicatorInset = 6.dp
private val IndicatorCorner = 28.dp
private val BottomFadeExtra = 36.dp

private val BarMinWidth = 200.dp
private val BarMaxWidth = 280.dp
private const val BarWidthFraction = 0.62f

private val IconSize = XrayDimens.IconLg
private const val IconSelectedScale = 1.15f
private val LabelFontSize = 11.sp
private val LabelLineHeight = 12.sp

private val TrailingSpacing = 12.dp


@Composable
fun rememberFloatingNavClearance(extraAboveBar: Dp = FloatingNavExtraContentPadding): Dp {
    val systemBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    return systemBottom + FloatingNavBarHeight + FloatingNavBottomMargin + extraAboveBar
}

@Composable
fun FloatingNavBottomFade(modifier: Modifier = Modifier) {
    val fadeHeight = rememberFloatingNavClearance(extraAboveBar = BottomFadeExtra)
    val background = MaterialTheme.colorScheme.background
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(fadeHeight)
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.4f to background.copy(alpha = 0.55f),
                        0.72f to background.copy(alpha = 0.88f),
                        1f to background,
                    ),
                ),
    )
}

data class FloatingNavItem(
    val id: String,
    val icon: ImageVector,
    val label: String,
)

fun RootTab.toFloatingNavItem(): FloatingNavItem =
    when (this) {
        RootTab.Connection -> FloatingNavItem(id = name, icon = XrayIcons.HomeActive, label = "Connection")
        RootTab.Settings -> FloatingNavItem(id = name, icon = XrayIcons.SettingsActive, label = "Settings")
    }

/** Floating pill bottom nav — INCY style with purple gradient. */
@Composable
fun XrayFloatingNav(
    items: List<FloatingNavItem>,
    selectedId: String,
    onItemSelected: (FloatingNavItem) -> Unit,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null,
    onTrailingClick: (() -> Unit)? = null,
) {
    val density = LocalDensity.current
    val itemCount = items.size.coerceAtLeast(1)
    val selectedIndex = items.indexOfFirst { it.id == selectedId }.coerceAtLeast(0)
    val animOffsetX = remember { Animatable(0f) }

    val indicatorBrush =
        remember {
            Brush.linearGradient(
                colors = listOf(XrayBrand.BlueLight, XrayBrand.BlueDeep),
            )
        }
    val chromeColor = MaterialTheme.colorScheme.surfaceContainer
    val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant

    // Ширина доступного пространства в пикселях, полученная через onSizeChanged.
    var availableWidthPx by remember { mutableStateOf(0) }
    val barWidth =
        with(density) {
            if (availableWidthPx <= 0) {
                BarMaxWidth
            } else {
                (availableWidthPx.toDp() * BarWidthFraction)
                    .coerceAtMost(BarMaxWidth)
                    .coerceAtLeast(BarMinWidth)
            }
        }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .onSizeChanged { availableWidthPx = it.width },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier =
                Modifier
                    .width(barWidth)
                    .height(FloatingNavBarHeight)
                    .clip(RoundedCornerShape(BarCorner)),
            shape = RoundedCornerShape(BarCorner),
            color = chromeColor,
            shadowElevation = 10.dp,
            tonalElevation = 6.dp,
        ) {
            var containerSize by remember { mutableStateOf(IntSize.Zero) }

            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(BarCorner))
                        .onSizeChanged { containerSize = it },
            ) {
                val maxWidthPx = containerSize.width.toFloat().coerceAtLeast(1f)
                val slotWidthPx = maxWidthPx / itemCount
                val maxOffsetPx = (maxWidthPx - slotWidthPx).coerceAtLeast(0f)
                // Равномерное распределение позиций индикатора, чтобы последний
                // слот не смещался из-за накопления ошибок округления.
                val targetOffsetPx =
                    if (itemCount <= 1) {
                        0f
                    } else {
                        maxOffsetPx * selectedIndex / (itemCount - 1)
                    }

                LaunchedEffect(selectedIndex, itemCount, maxWidthPx) {
                    animOffsetX.snapTo(animOffsetX.value.coerceIn(0f, maxOffsetPx))
                    animOffsetX.animateTo(
                        targetValue = targetOffsetPx.coerceIn(0f, maxOffsetPx),
                        animationSpec =
                            spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium,
                            ),
                    )
                }

                val slotWidthDp = with(density) { slotWidthPx.toDp() }

                Box(
                    modifier =
                        Modifier
                            .offset {
                                IntOffset(
                                    animOffsetX.value.coerceIn(0f, maxOffsetPx).roundToInt(),
                                    0,
                                )
                            }
                            .width(slotWidthDp)
                            .fillMaxHeight()
                            .padding(IndicatorInset)
                            .clip(RoundedCornerShape(IndicatorCorner))
                            .background(indicatorBrush),
                )

                Row(modifier = Modifier.fillMaxSize()) {
                    items.forEachIndexed { index, item ->
                        val selected = index == selectedIndex
                        val iconScale by
                        animateFloatAsState(
                            targetValue = if (selected) IconSelectedScale else 1f,
                            animationSpec = tween(300),
                        )
                        val contentColor by
                        animateColorAsState(
                            targetValue = if (selected) Color.White else unselectedColor,
                            animationSpec = tween(300),
                        )
                        // Всегда резервируем место под подпись, чтобы иконка
                        // не «прыгала» при смене выделения.
                        val labelAlpha by
                        animateFloatAsState(
                            targetValue = if (selected) 1f else 0f,
                            animationSpec = tween(300),
                        )

                        Box(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                    ) { onItemSelected(item) }
                                    .semantics {
                                        semanticsSelected = selected
                                        role = Role.Tab
                                    },
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(horizontal = 4.dp),
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    tint = contentColor,
                                    modifier =
                                        Modifier
                                            .size(IconSize)
                                            .scale(iconScale),
                                )
                                Text(
                                    text = item.label,
                                    color = contentColor,
                                    fontSize = LabelFontSize,
                                    lineHeight = LabelLineHeight,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    modifier =
                                        Modifier
                                            .padding(top = 2.dp)
                                            .alpha(labelAlpha),
                                )
                            }
                        }
                    }
                }
            }
        }

        if (trailingContent != null) {
            Box(
                modifier =
                    Modifier
                        .width(TrailingSpacing)
                        .height(FloatingNavBarHeight),
            )
            Surface(
                modifier =
                    Modifier
                        .size(FloatingNavBarHeight)
                        .clip(CircleShape)
                        .then(
                            if (onTrailingClick != null) {
                                Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = onTrailingClick,
                                )
                            } else {
                                Modifier
                            },
                        ),
                shape = CircleShape,
                color = chromeColor,
                shadowElevation = 10.dp,
                tonalElevation = 6.dp,
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    trailingContent()
                }
            }
        }
    }
}