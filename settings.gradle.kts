pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}


rootProject.name = "TesUtility"
include(":androidApp")
include(":tun2socks")
include(":common")
include(":domain")
include(":core:database")
include(":core:data")
include(":core:network")
include(":core:datastore")
include(":core:native-bridge")
include(":platform:vpn")
include(":shared")
include(":webApp")
