package com.ivis.qcauditapp.models

data class TicketResponse(
    val results: List<Ticket>,
    val errorMessage: String?,
    val errorCode: String
)

data class Ticket(
    val ticketAuditId: Int,
    val ticketId: Int,
    val date: String?,
    val siteName: String?,
    val task: String?,
    val ticketStatus: String?,
    val attended: String,
    val ta: Int,
    val downType: String?,
    val description: String?,
    val fieldRep: String,
    val priority: Int,
    val loginTime: String?,
    val lastVisited: String?,
    val dependency: String?,
    val spareReceived: String?,
    val unitId: String?
)

