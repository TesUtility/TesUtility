package com.android.xrayfa.common.repository

import com.android.xrayfa.common.routing.DomainStrategy
import com.android.xrayfa.common.routing.RoutingMode
import com.android.xrayfa.common.routing.Rule
import kotlinx.coroutines.flow.Flow

interface SettingsRepository : ConfigParserSettingsProvider {
    val settingsFlow: Flow<SettingsState>
    val packagesFlow: Flow<List<String>>

    suspend fun setRoutingMode(mode: RoutingMode)
    suspend fun setDarkMode(darkMode: Theme)
    suspend fun setDomainStrategy(domainStrategy: DomainStrategy)
    suspend fun setRoutingRules(rules: List<Rule>)
    suspend fun setIpV6Enable(enable: Boolean)
    suspend fun setSocksPort(port: Int)
    suspend fun setHttpPort(port: Int)
    suspend fun setLanHttpProxyEnable(enable: Boolean)
    suspend fun setDnsIPv4(dns: String)
    suspend fun setDnsIPv6(dns: String)
    suspend fun setXrayCoreVersion(version: String)
    suspend fun setDelayTestUrl(url: String)
    suspend fun setAllowedPackages(packages: List<String>)
    suspend fun setGeoLiteInstall(installed: Boolean)
    suspend fun setLiveUpdateNotification(enable: Boolean)
    suspend fun setBootAutoStart(enable: Boolean)
    suspend fun setHexTunState(enable: Boolean)
    suspend fun setHideFromRecentsState(enable: Boolean)
    suspend fun setSocksUsername(username: String)
    suspend fun setSocksPassword(password: String)
    suspend fun setSocksListen(address: String)
    suspend fun setSendHwid(enable: Boolean)
    suspend fun setAgentFunctionsEnabled(enable: Boolean)
    suspend fun addAllowedPackages(packageName: String)
    suspend fun removeAllowedPackage(packageName: String)
    suspend fun getAllowedPackages(): List<String>
}
