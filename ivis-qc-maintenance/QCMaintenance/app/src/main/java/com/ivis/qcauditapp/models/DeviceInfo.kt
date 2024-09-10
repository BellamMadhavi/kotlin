package com.ivis.qcauditapp.models

import java.util.*

data class DeviceInfo(
    val id: Int,
    val deviceId: String,
    val active: Boolean,
    val version: String,
    val connected: Boolean,
    val lastPingTime: LastPingTime,
    val connectedTime: ConnectedTime,
    val siteName: String,
    val deviceIP: String,
    val duration: String,
    val primaryNetwork: Boolean,
    val oldVersion: Boolean
)

data class LastPingTime(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val hourOfDay: Int,
    val minute: Int,
    val second: Int
)

data class ConnectedTime(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val hourOfDay: Int,
    val minute: Int,
    val second: Int
)

