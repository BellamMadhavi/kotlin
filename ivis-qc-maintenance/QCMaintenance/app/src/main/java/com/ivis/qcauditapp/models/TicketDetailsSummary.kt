package com.ivis.qcauditapp.models

data class Task(
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

data class IndentRequest(
    val name: String,
    val qty: String,
    val id: Int
)

data class TicketDetailsSummary(
    val ticketAuditId: Int,
    val ticketId: Int,
    val tasks: List<Task>,
    val attachments: List<String>,
    val usedSpareIds: List<Int>,
    val indentRequest: List<IndentRequest>
)

