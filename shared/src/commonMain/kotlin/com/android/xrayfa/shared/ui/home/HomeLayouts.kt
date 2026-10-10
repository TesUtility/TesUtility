package com.android.xrayfa.shared.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.android.xrayfa.shared.ui.config.IncyAddPopupMenu
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.xrayfa.common.core.homeDelayTestEnabled
import com.android.xrayfa.shared.navigation.HomeState
import com.android.xrayfa.shared.resources.Res
import com.android.xrayfa.shared.resources.home_no_servers
import com.android.xrayfa.shared.resources.home_no_servers_hint
import com.android.xrayfa.shared.ui.theme.XrayBrand
import org.jetbrains.compose.resources.stringResource

internal data class HomeLayoutCallbacks(
    val onConnectToggle: () -> Unit,
    val onTestDelay: () -> Unit,
    val onOpenConfigPopup: (() -> Unit)? = null,
)

@Composable
internal fun HomeBrandHeroLayout(
    state: HomeState,
    elapsedSeconds: Long = 0L,
    labels: HomeUiLabels,
    showNodeCard: Boolean,
    callbacks: HomeLayoutCallbacks,
    scrollEnabled: Boolean,
    largeStatusLabel: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .then(if (scrollEnabled) Modifier.verticalScroll(rememberScrollState()) else Modifier)
                .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(12.dp))
        HomeConnectButton(
            isConnected = state.isConnected,
            enabled = !state.busy,
            onToggle = callbacks.onConnectToggle,
            elapsedSeconds = elapsedSeconds,
        )
        Spacer(Modifier.height(8.dp))
        HomeConnectionStatusLabel(
            isConnected = state.isConnected,
            connectedLabel = labels.connectedLabel,
            disconnectedLabel = labels.disconnectedLabel,
            connectedHint = labels.connectedHint,
            disconnectedHint = labels.disconnectedHint,
            large = largeStatusLabel,
        )
        Spacer(Modifier.height(28.dp))
        HomeSessionBundleCard(
            state = state,
            labels = labels,
            showNodeCard = showNodeCard,
            onTestDelay = callbacks.onTestDelay,
            onOpenConfigPopup = callbacks.onOpenConfigPopup,
        )
        HomeLayoutErrors(state = state, labels = labels)
    }
}

@Composable
private fun HomeSessionBundleCard(
    state: HomeState,
    labels: HomeUiLabels,
    showNodeCard: Boolean,
    onTestDelay: () -> Unit,
    onOpenConfigPopup: (() -> Unit)? = null,
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            HomeTrafficStatusCard(
                isConnected = state.isConnected,
                uploadSpeedKbps = state.uploadSpeedKbps,
                downloadSpeedKbps = state.downloadSpeedKbps,
                uploadLabel = labels.uploadLabel,
                downloadLabel = labels.downloadLabel,
                framed = false,
            )
            if (showNodeCard) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                HomeNodeBlock(
                    state = state,
                    labels = labels,
                    onTestDelay = onTestDelay,
                    onOpenConfigPopup = onOpenConfigPopup,
                    framed = false,
                )
            }
        }
    }
}

@Composable
private fun HomeNodeBlock(
    state: HomeState,
    labels: HomeUiLabels,
    onTestDelay: () -> Unit,
    onOpenConfigPopup: (() -> Unit)? = null,
    framed: Boolean,
) {
    val node = state.selectedNode
    val clickModifier = if (onOpenConfigPopup != null) {
        Modifier.clickable(onClick = onOpenConfigPopup)
    } else Modifier

    Box(modifier = clickModifier) {
        if (node != null) {
            HomeSelectedNodeCard(
                node = node,
                unknownProtocolLabel = labels.unknownProtocolLabel,
                countryEmoji = node.countryISO,
                delayMs = state.delayMs,
                testing = state.testing,
                enableTest = homeDelayTestEnabled(state.isConnected, state.testing),
                onTest = onTestDelay,
                framed = framed,
            )
        } else {
            HomeNoServersBlock(framed = framed)
        }
    }
}

@Composable
private fun ColumnScope.HomeLayoutErrors(
    state: HomeState,
    labels: HomeUiLabels,
) {
    if (state.showConfigError) {
        Spacer(Modifier.height(12.dp))
        HomeSectionHeader(
            text = labels.configNotReadyMessage,
            modifier = Modifier.fillMaxWidth(),
        )
    }
    state.connectionErrorMessage?.let { message ->
        Spacer(Modifier.height(8.dp))
        HomeSectionHeader(
            text = message,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * INCY-style empty state: folder icon, "No servers" title, two-line hint.
 * The quick-action row (Add / Paste / QR) lives in [com.android.xrayfa.shared.ui.RootContent].
 */
@Composable
internal fun HomeNoServersBlock(
    framed: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Outlined.Folder,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(56.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.home_no_servers),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.home_no_servers_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * INCY-style quick actions row shown on the Home tab when no node is selected:
 * `+ Add` (accent with pop-up menu) · `Paste` (neutral) · QR (icon square).
 */
@Composable
fun HomeQuickActions(
    addLabel: String,
    pasteLabel: String,
    scanQrContentDescription: String,
    onAdd: () -> Unit,
    onPaste: () -> Unit,
    onScanQr: () -> Unit,
    modifier: Modifier = Modifier,
    onManageSubscriptions: (() -> Unit)? = null,
) {
    var showAddMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Card(
                onClick = { showAddMenu = true },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = XrayBrand.Blue),
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text(addLabel, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            IncyAddPopupMenu(
                expanded = showAddMenu,
                onDismissRequest = { showAddMenu = false },
                onAddManual = onAdd,
                onImportFromClipboard = onPaste,
                onScanQr = onScanQr,
                onManageSubscriptions = onManageSubscriptions,
            )
        }

        Card(
            onClick = onPaste,
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Outlined.ContentCopy, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.width(6.dp))
                Text(pasteLabel, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            }
        }

        Card(
            onClick = onScanQr,
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.QrCode, contentDescription = scanQrContentDescription, tint = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}
