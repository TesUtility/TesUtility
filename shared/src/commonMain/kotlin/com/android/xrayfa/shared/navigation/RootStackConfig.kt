package com.android.xrayfa.shared.navigation

import com.android.xrayfa.agent.AgentScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface RootStackConfig {
    @Serializable data object Idle : RootStackConfig
    @Serializable data object Config : RootStackConfig
    @Serializable data object Subscriptions : RootStackConfig
    @Serializable data object QrScanner : RootStackConfig
    @Serializable data object Apps : RootStackConfig
    @Serializable data object Logcat : RootStackConfig
    @Serializable data object RouteSettings : RootStackConfig
    @Serializable data class NodeEdit(val nodeId: Int) : RootStackConfig
}

data class RootNavigationTarget(
    val tab: RootTab? = null,
    val stack: RootStackConfig = RootStackConfig.Idle,
)

fun AgentScreen.toRootNavigation(): RootNavigationTarget =
    when (this) {
        AgentScreen.Home -> RootNavigationTarget(tab = RootTab.Connection, stack = RootStackConfig.Idle)
        AgentScreen.Config -> RootNavigationTarget(tab = RootTab.Connection, stack = RootStackConfig.Config)
        AgentScreen.Subscriptions ->
            RootNavigationTarget(tab = RootTab.Connection, stack = RootStackConfig.Subscriptions)
        AgentScreen.Settings -> RootNavigationTarget(tab = RootTab.Settings, stack = RootStackConfig.Idle)
        AgentScreen.Apps -> RootNavigationTarget(stack = RootStackConfig.Apps)
        AgentScreen.RouteSettings -> RootNavigationTarget(stack = RootStackConfig.RouteSettings)
    }
