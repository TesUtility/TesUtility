package com.android.xrayfa.common.repository

import com.android.xrayfa.common.routing.DomainStrategy
import com.android.xrayfa.common.routing.RoutingMode
import com.android.xrayfa.common.routing.defaultRoutes

data class SettingsState(
    val darkMode: Int = 0,
    val ipV6Enable: Boolean = false,
    val socksPort: Int = 10808,
    val httpPort: Int = 10809,
    val lanHttpProxyEnable: Boolean = false,
    val socksUserName: String = "",
    val socksPassword: String = "",
    val socksListen: String = "",
    val dnsIPv4: String = "",
    val dnsIPv6: String = "",
    val delayTestUrl: String = "https://www.google.com",
    val xrayCoreVersion: String = "unknown",
    val version: String = "1.0.0",
    val geoLiteInstall: Boolean = false,
    val liveUpdateNotification: Boolean = false,
    val bootAutoStart: Boolean = false,
    val hexTunEnable: Boolean = true,
    val hideFromRecents: Boolean = false,
    val domainStrategy: Int = DomainStrategy.IP_IF_NON_MATCH.code,
    val routingRules: String = defaultRoutes,
    val routingMode: Int = RoutingMode.ROUTE.code,
    val hwid: String = "",
    val sendHwid: Boolean = true,
    val allowedPackages: List<String> = emptyList(),
    val agentFunctionsEnabled: Boolean = false,
)
