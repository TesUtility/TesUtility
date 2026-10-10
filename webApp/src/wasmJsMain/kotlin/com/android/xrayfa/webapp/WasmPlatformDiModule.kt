package com.android.xrayfa.webapp

import com.android.xrayfa.common.core.CoreStartOptions
import com.android.xrayfa.common.core.GeoIpProvider
import com.android.xrayfa.common.core.XrayAssetPaths
import com.android.xrayfa.common.core.XrayCore
import com.android.xrayfa.network.FileDownloader
import com.android.xrayfa.shared.vpn.TrafficStatsSource
import com.android.xrayfa.shared.vpn.VpnConnectCoordinator
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flowOf
import org.koin.dsl.module

val wasmPlatformDiModule = module {
    single<XrayAssetPaths> { WasmXrayAssetPaths }
    single<GeoIpProvider> { WasmGeoIpProvider }
    single<TrafficStatsSource> { WasmTrafficStatsSource }
    single<VpnConnectCoordinator> { WasmVpnConnectCoordinator }
    single<XrayCore> { WasmXrayCore }
    single { FileDownloader(HttpClient(Js)) }
}

private object WasmXrayAssetPaths : XrayAssetPaths {
    override val basePath: String = "/wasm-assets"
    override val geoIpPath: String = "/wasm-assets/geoip.dat"
    override val geoSitePath: String = "/wasm-assets/geosite.dat"
    override val geoLiteDatabasePath: String = "/wasm-assets/GeoLite2-Country.mmdb"
}

private object WasmGeoIpProvider : GeoIpProvider {
    override fun countryIsoFromIp(ip: String): String = ""
}

private object WasmTrafficStatsSource : TrafficStatsSource {
    override val speedsKbps: Flow<Pair<Double, Double>> = flowOf(0.0 to 0.0)
}

private object WasmVpnConnectCoordinator : VpnConnectCoordinator {
    override suspend fun prepareConfigForConnect(): Boolean = false
    override suspend fun connect(): Boolean = false
    override fun disconnect() {}
}

private object WasmXrayCore : XrayCore {
    override val trafficFlow: SharedFlow<Pair<Double, Double>> = MutableSharedFlow()

    override fun startTrafficDetection() {}
    override fun stopTrafficDetection() {}

    override suspend fun startXrayCore(startOptions: CoreStartOptions, tunFd: Int?): Boolean = false
    override fun stopXrayCore() {}
    override fun measureDelaySync(url: String): Long = -1L
    override fun measureOutboundDelay(config: String, url: String): Long = -1L
}