package com.ivis.qcauditapp.viewmodels.maintenance.tickets

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivis.qcauditapp.models.TicketDetailsResponse
import com.qcauditapp.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class TicketDetailsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val ticketdetailsdatalist: LiveData<TicketDetailsResponse>
        get() = repository.DetailsData

    fun fetchTicketDetailsdata(ticketAuditId: Int?, authorization: String, Customer_Id: String, Tenant_Id: Int) {
        viewModelScope.launch {
            repository.getticketdetailslist(ticketAuditId, authorization, Customer_Id, Tenant_Id)
        }
    }
}