package com.android.xrayfa.shared.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume

fun createRootComponent(
    componentContext: ComponentContext,
    filterLabels: ConfigFilterLabels = ConfigFilterLabels(),
): RootComponent =
    DefaultRootComponent(
        componentContext = componentContext,
        configComponentFactory = defaultConfigComponentFactory(filterLabels),
    )

fun createRootComponent(
    lifecycle: LifecycleRegistry,
    filterLabels: ConfigFilterLabels = ConfigFilterLabels(),
): RootComponent =
    createRootComponent(DefaultComponentContext(lifecycle = lifecycle), filterLabels)

/** Convenience for previews/tests; caller must [LifecycleRegistry.resume] before use. */
fun createRootComponent(
    filterLabels: ConfigFilterLabels = ConfigFilterLabels(),
): RootComponent {
    val lifecycle = LifecycleRegistry()
    lifecycle.resume()
    return createRootComponent(lifecycle, filterLabels)
}
