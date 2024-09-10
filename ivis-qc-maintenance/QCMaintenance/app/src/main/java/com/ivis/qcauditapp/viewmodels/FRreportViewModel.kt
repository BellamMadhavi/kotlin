package com.ivis.qcauditapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivis.qcauditapp.models.MaintenanceResponse
import com.ivis.qcauditapp.models.MaintenancefrreportsData
import com.qcauditapp.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class FRreportViewModel  @Inject constructor(private val repository: Repository) : ViewModel() {
    val FrreportDatalist: LiveData<MaintenancefrreportsData>
        get() = repository.FrreportData

    fun fetchfrreportlist(userid : Int, authorization: String, Customer_Id : String, Tenant_Id  : Int) {
        viewModelScope.launch {
            repository.getreportslist(userid, authorization, Customer_Id, Tenant_Id)
        }
    }
}