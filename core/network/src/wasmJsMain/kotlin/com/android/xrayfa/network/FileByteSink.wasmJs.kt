package com.android.xrayfa.network

actual class FileByteSink actual constructor(path: String) {
    actual fun write(buffer: ByteArray, offset: Int, length: Int) {}
    actual fun close() {}
}
