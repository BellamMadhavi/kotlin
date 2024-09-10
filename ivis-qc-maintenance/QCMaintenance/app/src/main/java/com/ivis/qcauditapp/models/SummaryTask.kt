package com.ivis.qcauditapp.models


data class SummaryTask(
    val ticketId: Int,
    val taskId: Int,
    val taskTitle: String,
    val status: String,
    val openReason: String,
    val dependency: String,
    val fixedType: String?,
    val fixedReason: String?,
    val remarks: String,
    val sparesUsed: Boolean,
)