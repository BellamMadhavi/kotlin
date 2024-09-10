package com.ivis.qcauditapp.viewmodels.maintenance

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivis.qcauditapp.models.IndentSpareDetails
import com.ivis.qcauditapp.models.RequestSpareItem
import com.qcauditapp.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class RaiseRequestViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val RaiseRequestlist: LiveData<RequestSpareItem>
        get() = repository.RaiseRequest

    fun fetchraiserequest(authorization: String, Customer_Id : String, Tenant_Id  : Int) {
        viewModelScope.launch {
            repository.getraiserequest(authorization, Customer_Id, Tenant_Id)
        }
    }
}