package com.android.xrayfa.webapp

import com.android.xrayfa.common.repository.ConfigParserSettings
import com.android.xrayfa.common.repository.SettingsRepository
import com.android.xrayfa.common.repository.SettingsState
import com.android.xrayfa.common.repository.Theme
import com.android.xrayfa.common.routing.DomainStrategy
import com.android.xrayfa.common.routing.RoutingMode
import com.android.xrayfa.common.routing.Rule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WasmSettingsRepository : SettingsRepository {
    private val _settings = MutableStateFlow(SettingsState())
    override val settingsFlow: Flow<SettingsState> = _settings.asStateFlow()

    private val _packages = MutableStateFlow<List<String>>(emptyList())
    override val packagesFlow: Flow<List<String>> = _packages.asStateFlow()

    override suspend fun setRoutingMode(mode: RoutingMode) {
        _settings.value = _settings.value.copy(routingMode = mode.code)
    }

    override suspend fun setDarkMode(darkMode: Theme) {
        _settings.value = _settings.value.copy(darkMode = darkMode.code)
    }

    override suspend fun setDomainStrategy(domainStrategy: DomainStrategy) {
        _settings.value = _settings.value.copy(domainStrategy = domainStrategy.code)
    }

    override suspend fun setRoutingRules(rules: List<Rule>) {
    }

    override suspend fun setIpV6Enable(enable: Boolean) {
        _settings.value = _settings.value.copy(ipV6Enable = enable)
    }

    override suspend fun setSocksPort(port: Int) {
        _settings.value = _settings.value.copy(socksPort = port)
    }

    override suspend fun setHttpPort(port: Int) {
        _settings.value = _settings.value.copy(httpPort = port)
    }

    override suspend fun setLanHttpProxyEnable(enable: Boolean) {
        _settings.value = _settings.value.copy(lanHttpProxyEnable = enable)
    }

    override suspend fun setDnsIPv4(dns: String) {
        _settings.value = _settings.value.copy(dnsIPv4 = dns)
    }

    override suspend fun setDnsIPv6(dns: String) {
        _settings.value = _settings.value.copy(dnsIPv6 = dns)
    }

    override suspend fun setXrayCoreVersion(version: String) {
        _settings.value = _settings.value.copy(xrayCoreVersion = version)
    }

    override suspend fun setDelayTestUrl(url: String) {
        _settings.value = _settings.value.copy(delayTestUrl = url)
    }

    override suspend fun setAllowedPackages(packages: List<String>) {
        _packages.value = packages
    }

    override suspend fun setGeoLiteInstall(installed: Boolean) {
        _settings.value = _settings.value.copy(geoLiteInstall = installed)
    }

    override suspend fun setLiveUpdateNotification(enable: Boolean) {
        _settings.value = _settings.value.copy(liveUpdateNotification = enable)
    }

    override suspend fun setBootAutoStart(enable: Boolean) {
        _settings.value = _settings.value.copy(bootAutoStart = enable)
    }

    override suspend fun setHexTunState(enable: Boolean) {
        _settings.value = _settings.value.copy(hexTunEnable = enable)
    }

    override suspend fun setHideFromRecentsState(enable: Boolean) {
        _settings.value = _settings.value.copy(hideFromRecents = enable)
    }

    override suspend fun setSocksUsername(username: String) {
        _settings.value = _settings.value.copy(socksUserName = username)
    }

    override suspend fun setSocksPassword(password: String) {
        _settings.value = _settings.value.copy(socksPassword = password)
    }

    override suspend fun setSocksListen(address: String) {
        _settings.value = _settings.value.copy(socksListen = address)
    }

    override suspend fun setSendHwid(enable: Boolean) {
        _settings.value = _settings.value.copy(sendHwid = enable)
    }

    override suspend fun setAgentFunctionsEnabled(enable: Boolean) {
        _settings.value = _settings.value.copy(agentFunctionsEnabled = enable)
    }

    override suspend fun addAllowedPackages(packageName: String) {
        val current = _packages.value.toMutableList()
        if (!current.contains(packageName)) {
            current.add(packageName)
            _packages.value = current
        }
    }

    override suspend fun removeAllowedPackage(packageName: String) {
        _packages.value = _packages.value.filter { it != packageName }
    }

    override suspend fun getAllowedPackages(): List<String> = _packages.value

    override suspend fun getConfigParserSettings(): ConfigParserSettings {
        val settings = _settings.value
        return ConfigParserSettings(
            socksListen = settings.socksListen,
            socksPort = settings.socksPort,
            socksUserName = settings.socksUserName,
            socksPassword = settings.socksPassword,
            lanHttpProxyEnable = settings.lanHttpProxyEnable,
            httpPort = settings.httpPort,
            dnsIPv4 = settings.dnsIPv4,
            dnsIPv6 = settings.dnsIPv6,
            ipV6Enable = settings.ipV6Enable,
            domainStrategy = settings.domainStrategy,
            routingRules = settings.routingRules,
            routingMode = settings.routingMode,
            geoLiteInstall = settings.geoLiteInstall,
        )
    }
}
