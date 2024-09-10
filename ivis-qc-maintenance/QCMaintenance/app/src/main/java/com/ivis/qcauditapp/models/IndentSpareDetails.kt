package com.ivis.qcauditapp.models


data class IndentSpareDetails(
    val results: ResultsData,
    val errorMessage: String?,
    val errorCode: String
)

data class ResultsData(
    val spareBag: List<SpareBagItem>,
    val indents: List<IndentItem>
)

data class SpareBagItem(
    val id: Int,
    val reqType: String,
    val itemDescription: String,
    val qty: Int,
    val status: String,
    val serialNo: String,
    val docketNumber: String,
    val dcNo: String,
    val dispatchedDate: String,
    val receivedDate: String
)

data class IndentItem(
    val id: Int,
    val reqType: String,
    val itemDescription: String,
    val qty: Int,
    val status: String,
    val serialNo: String,
    val docketNumber: String?,
    val dcNo: String?,
    val dispatchedDate: String?,
    val receivedDate: String?,
    val ticketId: Int,
    val taskId: Int,
    var ischecked: Boolean
)


