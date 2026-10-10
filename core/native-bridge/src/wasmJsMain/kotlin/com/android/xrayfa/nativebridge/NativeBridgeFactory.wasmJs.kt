package com.android.xrayfa.nativebridge

actual object NativeBridgeFactory {
    actual fun createXrayBridge(): XrayBridge = WasmXrayBridge
    actual fun createTunBridge(): TunBridge = WasmTunBridge
}

private object WasmTunBridge : TunBridge {
    override fun startTun2Socks(configPath: String, tunFd: Int): Boolean = false
    override fun stopTun2Socks(): Boolean = true
    override fun isRunning(): Boolean = false
}

private object WasmXrayBridge : XrayBridge {
    override fun initCoreEnv(basePath: String, deviceId: String) {}

    override fun newCoreController(callback: XrayCoreCallback): XrayCoreController = WasmCoreController

    override fun checkVersion(): String = "XrayFA Web Wasm-1.7.1"

    override fun measureOutboundDelay(configJson: String, url: String): Long = -1L
}

private object WasmCoreController : XrayCoreController {
    override val isRunning: Boolean = false
    override fun startLoop(configJson: String, tunFd: Int) {}
    override fun stopLoop() {}
    override fun measureDelay(url: String): Long = -1L
    override fun queryAllOutboundTrafficStats(): String = ""
}
