package com.ivis.qcauditapp.viewmodels.maintenance

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivis.qcauditapp.models.RequestSpareItem
import com.ivis.qcauditapp.models.TicketDetailsSummaryResponse
import com.qcauditapp.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

class SubmitSummaryViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val submitTicketdata: LiveData<TicketDetailsSummaryResponse>
        get() = repository.TicketSummary

    fun submitSummarydata(authorization: String, Customer_Id : String, Tenant_Id  : Int,requestBody: RequestBody) {
        viewModelScope.launch {
            repository.submitSummarydata(authorization, Customer_Id, Tenant_Id, requestBody)
        }
    }
}