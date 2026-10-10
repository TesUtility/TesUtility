package com.android.xrayfa.shared.navigation

import kotlinx.serialization.Serializable

/** Top-level pager tabs — Connection and Settings. Config is a popup overlay. */
@Serializable
enum class RootTab {
    Connection,
    Settings,
}
