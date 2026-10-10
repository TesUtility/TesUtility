import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("webApp")
        browser {
            commonWebpackConfig {
                outputFileName = "webApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    open = false
                    port = 8080
                    static = (static ?: mutableListOf()).apply {
                        add(project.rootDir.path)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        wasmJsMain {
            dependencies {
                implementation(project(":shared"))
                implementation(project(":platform:vpn"))
                implementation(project(":common"))
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(libs.decompose)
                implementation(libs.decompose.extensions.compose)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.ktor.client.js)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.android.xrayfa.shared.resources"
    generateResClass = auto
}
