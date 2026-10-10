package com.android.xrayfa.vpn

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Web browser stub for [VpnController].
 * Browser sandboxes do not allow raw OS-level VPN adapters.
 */
class WasmVpnController : VpnController {
    private val _state = MutableStateFlow<VpnState>(VpnState.Disconnected)
    override val state: StateFlow<VpnState> = _state.asStateFlow()

    private val _connectError = MutableStateFlow<String?>(null)
    override val connectError: StateFlow<String?> = _connectError.asStateFlow()

    override suspend fun connect(): Boolean {
        _connectError.value = "VPN connections cannot run inside a web browser sandbox."
        _state.value = VpnState.Disconnected
        return false
    }

    override fun disconnect() {
        _state.value = VpnState.Disconnected
        _connectError.value = null
    }

    override suspend fun restartIfNeeded() {
        disconnect()
    }
}
