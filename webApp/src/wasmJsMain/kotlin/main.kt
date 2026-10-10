import com.android.xrayfa.webapp.wasmPlatformDiModule
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.android.xrayfa.common.repository.ConfigParserSettingsProvider
import com.android.xrayfa.common.repository.SettingsRepository
import com.android.xrayfa.common.utils.Logger
import com.android.xrayfa.config.KotlinxXrayConfigEncoder
import com.android.xrayfa.config.XrayConfigEncoder
import com.android.xrayfa.di.parserDiModule
import com.android.xrayfa.nativebridge.NativeBridgeFactory
import com.android.xrayfa.repository.NodeRepository
import com.android.xrayfa.repository.SubscriptionRepository
import com.android.xrayfa.shared.di.sharedServicesDiModule
import com.android.xrayfa.shared.navigation.createRootComponent
import com.android.xrayfa.shared.platform.ClipboardReader
import com.android.xrayfa.shared.platform.ClipboardWriter
import com.android.xrayfa.shared.ui.AppShell
import com.android.xrayfa.vpn.VpnController
import com.android.xrayfa.vpn.WasmVpnController
import com.android.xrayfa.webapp.WasmClipboardReader
import com.android.xrayfa.webapp.WasmClipboardWriter
import com.android.xrayfa.webapp.WasmNodeRepository
import com.android.xrayfa.webapp.WasmSettingsRepository
import com.android.xrayfa.webapp.WasmSubscriptionRepository
import kotlinx.browser.document
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

private object WasmLogger : Logger {
    override fun i(tag: String, message: String) {}
    override fun e(tag: String, message: String, throwable: Throwable?) {}
}

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    if (GlobalContext.getOrNull() == null) {
        val wasmSettingsRepository = WasmSettingsRepository()
        startKoin {
            modules(
                wasmPlatformDiModule,
                parserDiModule(),
                sharedServicesDiModule,
                module {
                    single<Logger> { WasmLogger }
                    single<VpnController> { WasmVpnController() }
                    single<SettingsRepository> { wasmSettingsRepository }
                    single<ConfigParserSettingsProvider> { wasmSettingsRepository }
                    single { NativeBridgeFactory.createXrayBridge() }
                    single { NativeBridgeFactory.createTunBridge() }
                    single<NodeRepository> { WasmNodeRepository() }
                    single<SubscriptionRepository> { WasmSubscriptionRepository() }
                    single<ClipboardReader> { WasmClipboardReader() }
                    single<ClipboardWriter> { WasmClipboardWriter() }
                    single<XrayConfigEncoder> { KotlinxXrayConfigEncoder() }
                }
            )
        }
    }

    val rootComponent = createRootComponent()
    val body = checkNotNull(document.body) { "Document body is null" }

    ComposeViewport(body) {
        AppShell(rootComponent = rootComponent, applyTheme = true)
    }
}
