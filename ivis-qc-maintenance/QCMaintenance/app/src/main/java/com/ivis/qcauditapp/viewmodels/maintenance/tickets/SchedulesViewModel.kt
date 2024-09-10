package com.ivis.qcauditapp.viewmodels.maintenance.tickets

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivis.qcauditapp.models.TicketResponse
import com.qcauditapp.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SchedulesViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val TicketsData: LiveData<TicketResponse>
        get() = repository.scheduleData

    fun fetchSchedules(userId: Int, scheduleDate: String, authorization: String,Customer_Id : String,Tenant_Id : Int) {
        viewModelScope.launch {
            repository.getScheduleslist(userId, scheduleDate, authorization, Customer_Id, Tenant_Id)
        }
    }
}