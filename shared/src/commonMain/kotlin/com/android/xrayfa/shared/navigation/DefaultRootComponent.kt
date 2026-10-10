package com.android.xrayfa.shared.navigation

import com.android.xrayfa.agent.AgentScreen
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val homeComponentFactory: HomeComponentFactory = defaultHomeComponentFactory(),
    private val configComponentFactory: ConfigComponentFactory = defaultConfigComponentFactory(),
    private val settingsComponentFactory: SettingsComponentFactory = defaultSettingsComponentFactory(),
    private val subscriptionComponentFactory: SubscriptionComponentFactory = defaultSubscriptionComponentFactory(),
) : RootComponent,
    ComponentContext by componentContext {
    private val navigation = PagesNavigation<RootTab>()
    private val stackNavigation = StackNavigation<RootStackConfig>()

    override val settingsComponent: SettingsComponent =
        settingsComponentFactory(childContext("settings"))

    override val pages: Value<ChildPages<RootTab, RootComponent.Child>> =
        childPages(
            source = navigation,
            serializer = RootTab.serializer(),
            initialPages = {
                Pages(
                    items = listOf(RootTab.Connection, RootTab.Settings),
                    selectedIndex = RootTab.Connection.ordinal,
                )
            },
        ) { tab, childContext ->
            when (tab) {
                RootTab.Connection ->
                    RootComponent.Child.Connection(
                        homeComponentFactory(childContext),
                    )
                RootTab.Settings ->
                    RootComponent.Child.Settings(
                        settingsComponentFactory(childContext),
                    )
            }
        }

    override val stack =
        childStack(
            source = stackNavigation,
            serializer = RootStackConfig.serializer(),
            initialStack = { listOf(RootStackConfig.Idle) },
            handleBackButton = false,
            childFactory = { config, childContext ->
                when (config) {
                    RootStackConfig.Idle -> RootComponent.StackChild.Idle
                    RootStackConfig.Config -> RootComponent.StackChild.Config(configComponentFactory(childContext))
                    RootStackConfig.Subscriptions ->
                        RootComponent.StackChild.Subscriptions(subscriptionComponentFactory(childContext))
                    RootStackConfig.QrScanner -> RootComponent.StackChild.QrScanner
                    RootStackConfig.Apps -> RootComponent.StackChild.Apps
                    RootStackConfig.Logcat -> RootComponent.StackChild.Logcat
                    RootStackConfig.RouteSettings -> RootComponent.StackChild.RouteSettings
                    is RootStackConfig.NodeEdit -> RootComponent.StackChild.NodeEdit(config.nodeId)
                }
            },
        )

    private fun sameDestination(a: RootStackConfig, b: RootStackConfig): Boolean =
        when {
            a is RootStackConfig.NodeEdit && b is RootStackConfig.NodeEdit -> a.nodeId == b.nodeId
            a is RootStackConfig.NodeEdit || b is RootStackConfig.NodeEdit -> false
            else -> a == b
        }

    /**
     * Brings [config] to the top, dropping it (and everything above it) from its old slot,
     * then pushes it. `Idle` stays at the bottom.
     */
    private fun bringOrPush(config: RootStackConfig) {
        stackNavigation.navigate { current ->
            val base = if (current.firstOrNull() is RootStackConfig.Idle) current else listOf(RootStackConfig.Idle) + current
            val existingIndex = base.indexOfFirst { sameDestination(it, config) }
            if (existingIndex >= 0) {
                base.take(existingIndex + 1)
            } else {
                base + config
            }
        }
    }

    /**
     * Stack-only destinations: always sit on top of Idle.
     */
    private fun pushOnIdle(config: RootStackConfig) {
        stackNavigation.replaceAll(RootStackConfig.Idle, config)
    }

    private fun clearStack() {
        stackNavigation.replaceAll(RootStackConfig.Idle)
    }

    private fun isSubscriptionsActive(): Boolean =
        stack.value.active.configuration is RootStackConfig.Subscriptions

    override fun selectTab(index: Int) {
        clearStack()
        navigation.select(index = index)
    }

    override fun onPageSelected(index: Int) {
        if (stack.value.active.configuration !is RootStackConfig.Idle) return
        navigation.select(index = index)
    }

    override fun openConfig() {
        bringOrPush(RootStackConfig.Config)
    }

    override fun openSubscriptions() {
        bringOrPush(RootStackConfig.Subscriptions)
        navigation.select(index = RootTab.Connection.ordinal)
    }
    override fun openQrScanner() {
        val stayOnSubscriptions = isSubscriptionsActive()
        bringOrPush(RootStackConfig.QrScanner)
        if (!stayOnSubscriptions) {
            navigation.select(index = RootTab.Connection.ordinal)
        }
    }
    override fun openApps() = pushOnIdle(RootStackConfig.Apps)
    override fun openLogcat() = pushOnIdle(RootStackConfig.Logcat)
    override fun openRouteSettings() = pushOnIdle(RootStackConfig.RouteSettings)
    override fun openNodeEdit(nodeId: Int) = bringOrPush(RootStackConfig.NodeEdit(nodeId))

    override fun navigateBack() {
        stackNavigation.navigate { current ->
            if (current.size <= 1) current else current.dropLast(1)
        }
    }

    override fun openAgentScreen(screen: AgentScreen) {
        val target = screen.toRootNavigation()
        when (val dest = target.stack) {
            RootStackConfig.Idle -> target.tab?.let { selectTab(it) }
            RootStackConfig.Config -> openConfig()
            RootStackConfig.Subscriptions -> openSubscriptions()
            RootStackConfig.Apps -> openApps()
            RootStackConfig.RouteSettings -> openRouteSettings()
            RootStackConfig.QrScanner -> openQrScanner()
            RootStackConfig.Logcat -> openLogcat()
            is RootStackConfig.NodeEdit -> openNodeEdit(dest.nodeId)
        }
    }
}
