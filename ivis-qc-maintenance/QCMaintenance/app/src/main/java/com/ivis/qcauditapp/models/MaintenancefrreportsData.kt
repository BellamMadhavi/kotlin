package com.ivis.qcauditapp.models

data class MaintenancefrreportsData(
    val results: Data,
    val errorMessage: String?,
    val errorCode: String
)

data class Data(
    val tickets: Int,
    val dispatchIndents: Int,
    val used: Int,
    val toBeReturned: Int
)