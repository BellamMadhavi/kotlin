package com.ivis.qcauditapp.models


data class TicketDetailsResponse(
    val results: Results,
    val errorMessage: String?,
    val errorCode: String
)

data class Results(
    val spareIndent: List<SpareIndent>,
    val schInfoHist: List<SchInfoHist>,
    val ticketTask: List<TicketTask>,
    val comment: List<Any>,
    val ticketInfo: TicketInfo,
    val people: People
)

data class SchInfoHist(
    val auditId: Int,
    val frId: Int,
    val description: String?,
    val fieldRep: String,
    val scheduledDate: String,
    val visitStatus: String,
    val visitedDate: String?,
    val ticketStatus: String,
    val total: Double
)

data class TicketTask(
    val fixedType: String?,
    val closedBy: String,
    val fixedDate: String?,
    val dependency: String?,
    val footageFrom: String?,
    val queue: String,
    val openedBy: String,
    val cameraId: String?,
    val closedDate: String?,
    val title: String,
    val createdTime: String,
    val remarks: String,
    val openReason: String?,
    val fixedBy: String,
    val fixedReason: String?,
    val closingReason: String?,
    val openDate: String?,
    val taskId: Int,
    var status: String,
    val footageTo: String?,
    val taskCategory: String?
)

data class TicketInfo(
    val closedBy: String,
    val attachments: Any?,
    val fixedDate: String?,
    val description: String,
    val siteName: String,
    val project: String,
    val title: String,
    val assignedTo: String,
    val frName: String,
    val ticketAge: Int,
    val unitId: String,
    val openReason: String?,
    val createdTime: String,
    val fixedBy: String,
    val teamLead: String,
    val fieldRep: String,
    val siteReady: String,
    val closedReason: String?,
    val dependency: String?,
    val spareReached: String,
    val targetDate: String?,
    val ticketType: String,
    val priority: String,
    val siteDownType: String?,
    val creationType: String?,
    val createdBy: String,
    val closedDate: String?,
    val frId: Int,
    val siteId: Int,
    val closingType: String?,
    val ticketId: Int,
    val queue: String?,
    val status: String
)

data class People(
    val lastScheduledDate: String,
    val lastVisitedDate: String?,
    val createdTime: String,
    val scheduledTo: String,
    val visitedBy: String?,
    val createdby: String,
    val fixedDate: String?,
    val closedDate: String?,
    val fixedBy: String,
    val closedBy: String
)

data class SpareIndent(
    val indentId: Int,
    val ticketId: Int,
    val unitId: String,
    val indentStatus: String,
    val requestedDate: String,
    val dispatchedDate: String?, // Nullable because it can be null
    val itemName: String,
    val reqFieldRep: String,
    val teamLead: String,
    val receivedDate: String? // Nullable because it can be null
)

