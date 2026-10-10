package com.android.xrayfa.shared.ui.config

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.xrayfa.model.Node
import com.android.xrayfa.shared.navigation.ConfigComponent
import com.android.xrayfa.shared.navigation.ConfigFilterIds
import com.android.xrayfa.shared.ui.chrome.SharedUnderlineTabBar
import com.android.xrayfa.shared.ui.theme.XrayBrand
import com.android.xrayfa.shared.ui.theme.XrayDimens
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun SharedConfigSection(
    component: ConfigComponent,
    modifier: Modifier = Modifier,
    labels: ConfigUiLabels = ConfigUiLabels(),
    listState: LazyListState = rememberLazyListState(),
    nodeDelayMap: Map<Int, Long> = emptyMap(),
    showFilterBar: Boolean = true,
    filterTrailingContent: @Composable (() -> Unit)? = null,
    onNodeSelected: (Node) -> Unit = { component.onSelectNode(it.id) },
    onShareNode: ((Node) -> Unit)? = null,
    onEditNode: ((Node) -> Unit)? = null,
    onDeleteNode: ((Node) -> Unit)? = null,
    onTestNode: ((Node) -> Unit)? = null,
    enableNodeTest: Boolean = false,
    listModifier: Modifier = Modifier,
    listContentPadding: PaddingValues = PaddingValues(),
    nestedScrollConnection: androidx.compose.ui.input.nestedscroll.NestedScrollConnection? = null,
    rowModifier: (Node) -> Modifier = { Modifier },
    onEmptyAddClick: (() -> Unit)? = null,
) {
    val state by component.state.subscribeAsState()

    Column(modifier = modifier.fillMaxSize()) {
        if (showFilterBar) {
            SharedConfigFilterBar(
                component = component,
                trailingContent = filterTrailingContent,
            )
        }

        if (state.nodes.isEmpty()) {
            if (state.searchQuery.isNotBlank()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(labels.searchNoResultsLabel)
                }
            } else {
                SharedConfigEmptyContent(
                    modifier = Modifier.weight(1f),
                    labels = labels,
                    selectedFilterId = state.selectedFilterId,
                    onAddClick = onEmptyAddClick,
                )
            }
        } else {
            val scrollModifier =
                if (nestedScrollConnection != null) {
                    listModifier.nestedScroll(nestedScrollConnection)
                } else {
                    listModifier
                }
            LazyColumn(
                state = listState,
                modifier = scrollModifier,
                contentPadding = listContentPadding,
            ) {
                items(state.nodes, key = { it.id }) { node ->
                    val delayMs = nodeDelayMap[node.id] ?: -1L
                    Column {
                        SharedConfigNodeRow(
                            node = node,
                            labels = labels,
                            modifier = rowModifier(node),
                            selected = node.selected,
                            favorite = node.favorite,
                            delayMs = delayMs,
                            testing = delayMs == -1L && nodeDelayMap.containsKey(node.id),
                            enableTest = enableNodeTest,
                            countryEmoji = node.countryISO,
                            onChoose = { onNodeSelected(node) },
                            onFavorite = {
                                component.onToggleFavorite(node.id, !node.favorite)
                            },
                            onTest = onTestNode?.let { { it(node) } },
                            onShare = onShareNode?.let { { it(node) } },
                            onEdit = onEditNode?.let { { it(node) } },
                            onDelete = onDeleteNode?.let { { it(node) } },
                        )
                        if (node != state.nodes.last()) {
                            HorizontalDivider(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(start = 86.dp, end = 16.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SharedConfigFilterBar(
    component: ConfigComponent,
    modifier: Modifier = Modifier,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    val state by component.state.subscribeAsState()
    SharedUnderlineTabBar(
        items = state.filters,
        selectedKey = state.selectedFilterId,
        itemKey = { it.id },
        onSelect = { component.onSelectFilter(it.id) },
        label = { it.label },
        modifier = modifier,
        trailingContent = trailingContent,
    )
}

/**
 * Современная primary-кнопка: фиксированная высота, градиент, мягкая тень
 * и press-анимация масштаба.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncyPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "primaryButtonScale",
    )
    val corner = RoundedCornerShape(XrayDimens.CornerMd)
    val gradient =
        Brush.linearGradient(
            colors =
                if (enabled) {
                    listOf(XrayBrand.BlueLight, XrayBrand.BlueDeep)
                } else {
                    listOf(
                        XrayBrand.BlueLight.copy(alpha = 0.4f),
                        XrayBrand.BlueDeep.copy(alpha = 0.4f),
                    )
                },
        )

    Surface(
        shape = corner,
        color = Color.Transparent,
        shadowElevation = if (pressed) 2.dp else 6.dp,
        modifier =
            modifier
                .fillMaxWidth()
                .height(52.dp)
                .scale(scale)
                .clip(corner)
                .clickable(
                    enabled = enabled,
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(gradient, corner)
                    .padding(horizontal = XrayDimens.SpaceMd),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            content()
        }
    }
}

/** INCY-style secondary (ghost) button. */
@Composable
fun IncySecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Unspecified,
        ),
        elevation = ButtonDefaults.buttonElevation(
            pressedElevation = 1.dp,
            defaultElevation = 0.dp,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceContainer,
                    RoundedCornerShape(16.dp),
                )
                .padding(vertical = 10.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

@Composable
fun SharedConfigEmptyContent(
    labels: ConfigUiLabels,
    selectedFilterId: Int,
    modifier: Modifier = Modifier,
    onAddClick: (() -> Unit)? = null,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Folder icon (INCY style)
        Surface(
            modifier = Modifier.size(120.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = CircleShape,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, XrayBrand.Blue.copy(alpha = 0.3f)),
        ) {
            Icon(
                imageVector = Icons.Outlined.Subscriptions,
                contentDescription = null,
                modifier =
                    Modifier
                        .padding(30.dp)
                        .fillMaxSize(),
                tint = XrayBrand.BlueLight,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = labels.emptyTitle,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = labels.emptyHint,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        if (
            onAddClick != null &&
            (selectedFilterId == ConfigFilterIds.SUB_MANUAL || selectedFilterId == ConfigFilterIds.SUB_ALL)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            IncyPrimaryButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(XrayDimens.IconSm),
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = labels.createConfigLabel,
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
