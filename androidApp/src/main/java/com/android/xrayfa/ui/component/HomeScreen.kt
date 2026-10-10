package com.android.xrayfa.ui.component

import android.app.Activity
import android.net.VpnService
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.xrayfa.shared.navigation.HomeComponent
import com.android.xrayfa.shared.ui.SharedHomeSection
import com.android.xrayfa.shared.ui.rememberHomeUiLabels
import com.android.xrayfa.shared.ui.theme.XrayBrand
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun CompactHomeContent(
    homeComponent: HomeComponent,
    elapsedSeconds: Long,
    onOpenConfigPopup: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    val homeLabels = rememberHomeUiLabels()

    val vpnPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            homeComponent.onConnectToggle()
        }
    }

    SharedHomeSection(
        component = homeComponent,
        elapsedSeconds = elapsedSeconds,
        labels = homeLabels,
        modifier = Modifier.fillMaxSize(),
        onOpenConfigPopup = onOpenConfigPopup,
        onConnectToggle = {
            val state = homeComponent.state.value
            if (state.isConnected) {
                homeComponent.onConnectToggle()
                return@SharedHomeSection
            }
            val prepare = VpnService.prepare(context)
            if (prepare != null) {
                vpnPermissionLauncher.launch(prepare)
            } else {
                homeComponent.onConnectToggle()
            }
        },
    )
}

@Composable
fun ExpandedHomeContent(
    homeComponent: HomeComponent,
    elapsedSeconds: Long,
    onOpenConfigPopup: (() -> Unit)? = null,
) {
    val homeState by homeComponent.state.subscribeAsState()
    val selectedNode = homeState.selectedNode
    val context = LocalContext.current
    val homeLabels = rememberHomeUiLabels()

    val vpnPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            homeComponent.onConnectToggle()
        }
    }

    SharedHomeSection(
        component = homeComponent,
        elapsedSeconds = elapsedSeconds,
        labels = homeLabels,
        modifier = Modifier.fillMaxSize(),
        onOpenConfigPopup = onOpenConfigPopup,
        onConnectToggle = {
            val state = homeComponent.state.value
            if (state.isConnected) {
                homeComponent.onConnectToggle()
                return@SharedHomeSection
            }
            val prepare = VpnService.prepare(context)
            if (prepare != null) {
                vpnPermissionLauncher.launch(prepare)
            } else {
                homeComponent.onConnectToggle()
            }
        },
    )
}

@Composable
fun EmptyNodeCard(text: String, contentPadding: Dp = 28.dp) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Dns,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
