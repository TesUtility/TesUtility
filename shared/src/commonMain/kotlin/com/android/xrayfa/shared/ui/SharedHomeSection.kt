package com.android.xrayfa.shared.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.android.xrayfa.shared.navigation.HomeComponent
import com.android.xrayfa.shared.ui.home.HomeBrandHeroLayout
import com.android.xrayfa.shared.ui.home.HomeLayoutCallbacks
import com.android.xrayfa.shared.ui.home.HomeUiLabels
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun SharedHomeSection(
    component: HomeComponent,
    elapsedSeconds: Long = 0L,
    modifier: Modifier = Modifier,
    showNodeCard: Boolean = true,
    onConnectToggle: (() -> Unit)? = null,
    onOpenConfigPopup: (() -> Unit)? = null,
    labels: HomeUiLabels = HomeUiLabels(),
    scrollEnabled: Boolean = true,
    largeStatusLabel: Boolean = false,
) {
    val state by component.state.subscribeAsState()
    HomeBrandHeroLayout(
        state = state,
        labels = labels,
        showNodeCard = showNodeCard,
        elapsedSeconds = elapsedSeconds,
        callbacks = HomeLayoutCallbacks(
            onConnectToggle = onConnectToggle ?: component::onConnectToggle,
            onTestDelay = component::onTestDelay,
            onOpenConfigPopup = onOpenConfigPopup,
        ),
        scrollEnabled = scrollEnabled,
        largeStatusLabel = largeStatusLabel,
        modifier = modifier,
    )
}
