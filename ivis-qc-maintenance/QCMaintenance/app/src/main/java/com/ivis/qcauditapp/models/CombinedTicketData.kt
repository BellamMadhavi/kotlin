package com.ivis.qcauditapp.models

import com.ivis.qcauditapp.db.RaiseIndentSpare
import com.ivis.qcauditapp.db.SpareBagEntity
import com.ivis.qcauditapp.db.TaskEntity

data class CombinedTicketData(
    val ticket: List<TaskEntity>,
    val spareBags: List<SpareBagEntity>,
    val raiseIndentSpares: List<RaiseIndentSpare>
)