package com.android.xrayfa.database

import androidx.room.RoomDatabaseConstructor

actual object XrayFADatabaseConstructor : RoomDatabaseConstructor<XrayFADatabase> {
    override fun initialize(): XrayFADatabase {
        error("Room database is not initialized on WasmJS target")
    }
}
