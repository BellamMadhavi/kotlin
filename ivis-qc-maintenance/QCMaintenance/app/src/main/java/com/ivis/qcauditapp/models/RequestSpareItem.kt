package com.ivis.qcauditapp.models

data class RequestSpareItem(
    val results: List<Result>,
    val errorMessage: String?,
    val errorCode: String
)
data class Result(
    val id: Int,
    val name: String,
    var qty: Int
)
