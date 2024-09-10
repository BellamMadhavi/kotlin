package com.ivis.qcauditapp.models

data class MaintenanceResponse( val results: List<String>,
                                val errorMessage: String?,
                                val errorCode: String)
