package com.ivis.qcauditapp.viewmodels.maintenance.indent

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivis.qcauditapp.models.IndentSpareDetails
import com.qcauditapp.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SpareViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val SparesDatalist: LiveData<IndentSpareDetails>
        get() = repository.SpareData

    fun fetchspares(ticketId : Int, authorization: String, Customer_Id : String, Tenant_Id  : Int) {
        viewModelScope.launch {
            repository.getsparedetailslist(ticketId, authorization, Customer_Id, Tenant_Id)
        }
    }
}