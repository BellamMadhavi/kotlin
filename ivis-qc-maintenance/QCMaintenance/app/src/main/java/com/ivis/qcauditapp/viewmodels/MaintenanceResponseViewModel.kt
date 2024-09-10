package com.ivis.qcauditapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivis.qcauditapp.models.IndentSpareDetails
import com.ivis.qcauditapp.models.MaintenanceResponse
import com.qcauditapp.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MaintenanceResponseViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val FormDatalist: LiveData<MaintenanceResponse>
        get() = repository.TicketFormData

    fun fetchformdata(type : String,category : String, authorization: String, Customer_Id : String, Tenant_Id  : Int) {
        viewModelScope.launch {
            repository.getticketformlist(type,category, authorization, Customer_Id, Tenant_Id)
        }
    }


    val FormReasonDatalist: LiveData<MaintenanceResponse>
        get() = repository.TicketReasonFormData

    fun fetchReasonFormdata(type : String,category : String, authorization: String, Customer_Id : String, Tenant_Id  : Int) {
        viewModelScope.launch {
            repository.getticketReasonFormlist(type,category, authorization, Customer_Id, Tenant_Id)
        }
    }

}