package com.ivis.qcauditapp.retrofit

import com.ivis.qcauditapp.models.IndentItem
import com.ivis.qcauditapp.models.SpareBagItem
import com.ivis.qcauditapp.models.TicketTask

interface ItemOnClickListener {
    fun MaintanenceTaskClickListener(position: Int)
    fun MaintanenceTicketsClickListener(position: Int)
    fun onIndentitemClick(indentItem: IndentItem, position: Int)
    fun onSpareitemClick(spareitem : SpareBagItem, isSelected: Boolean)
    fun onRadioButtonClicked(position: Int, selectedOption: String, taskitem :TicketTask)
}