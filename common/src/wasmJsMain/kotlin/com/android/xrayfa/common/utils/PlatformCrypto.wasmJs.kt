package com.android.xrayfa.common.utils

import kotlin.random.Random

internal actual val defaultCryptoRandom: CryptoRandom = object : CryptoRandom {
    override fun nextBytes(buffer: ByteArray) {
        Random.nextBytes(buffer)
    }

    override fun nextInt(n: Int): Int = Random.nextInt(n)
}

internal actual val defaultDigestCalculator: DigestCalculator = object : DigestCalculator {
    override fun createDigest(algorithm: String): StreamingDigest = object : StreamingDigest {
        private var data = ByteArray(0)
        override fun update(buffer: ByteArray, offset: Int, length: Int) {
            data += buffer.copyOfRange(offset, offset + length)
        }
        override fun finalize(): ByteArray {
            return ByteArray(32) { (data.size + it).toByte() }
        }
    }
}
