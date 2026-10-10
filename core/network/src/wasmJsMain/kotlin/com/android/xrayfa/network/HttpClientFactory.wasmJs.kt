package com.android.xrayfa.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js

actual fun createSubscriptionHttpClient(userAgent: String): HttpClient {
    return HttpClient(Js)
}
