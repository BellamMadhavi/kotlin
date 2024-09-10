package com.qcauditapp.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ivis.qcauditapp.activities.SingleLiveEvent
import com.ivis.qcauditapp.db.RaiseIndentSpare
import com.ivis.qcauditapp.db.SpareBagEntity
import com.ivis.qcauditapp.db.TaskEntity
import com.ivis.qcauditapp.db.TicketImageEntity
import com.ivis.qcauditapp.db.UserDao
import com.ivis.qcauditapp.models.ApiResponse
import com.ivis.qcauditapp.models.CombinedTicketData
import com.ivis.qcauditapp.models.IndentSpareDetails
import com.ivis.qcauditapp.models.LoginRequest
import com.ivis.qcauditapp.models.MaintenanceResponse
import com.ivis.qcauditapp.models.MaintenancefrreportsData
import com.ivis.qcauditapp.models.RequestSpareItem
import com.ivis.qcauditapp.models.TicketDetailsResponse
import com.ivis.qcauditapp.models.TicketDetailsSummaryResponse
import com.ivis.qcauditapp.models.TicketResponse
import com.qcauditapp.retrofit.API
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Named

class Repository @Inject constructor(@Named("QCbaseUrl") private val QcBaseUrl: API,
                                     @Named("MaintenancebaseUrl") private val MaintenanceBaseurl: API,  private val userDao: UserDao) {

    private val account_data = SingleLiveEvent<ApiResponse>()
    val accountdata: LiveData<ApiResponse>
        get() = account_data


    private val error_data = SingleLiveEvent<String>()
    val errordata: LiveData<String>
        get() = error_data

    suspend fun getlogin(email: String, pwd: String) {
        val loginRequest = LoginRequest(email, pwd)
        val result = MaintenanceBaseurl. getlogin(loginRequest)
        if (result.isSuccessful && result.body() != null) {
            account_data.postValue(result.body())
        } else {
            when (result.code()) {
                400 -> error_data.postValue("Invalid credentials")
                else -> error_data.postValue("Error occured while logging in, Please try after some time.")
            }
        }
    }


    private val _scheduleData = SingleLiveEvent<TicketResponse>()
    val scheduleData: LiveData<TicketResponse>
        get() = _scheduleData

    suspend fun getScheduleslist(userId: Int, scheduleDate: String, authorization: String, Customer_Id : String, Tenant_Id  : Int) {
        val result = MaintenanceBaseurl.getSchedules(userId, scheduleDate, authorization, Customer_Id, Tenant_Id)
        if (result.isSuccessful && result.body() != null) {
            _scheduleData.postValue(result.body())
        }
    }

    private val _ticketDetailsData = SingleLiveEvent<TicketDetailsResponse>()
    val DetailsData: LiveData<TicketDetailsResponse>
        get() = _ticketDetailsData

    suspend fun getticketdetailslist(ticketAuditId : Int?, authorization: String, Customer_Id : String, Tenant_Id  : Int) {
        val result = MaintenanceBaseurl.getTicketDetails(ticketAuditId, authorization, Customer_Id, Tenant_Id)
        if (result.isSuccessful && result.body() != null) {
            _ticketDetailsData.postValue(result.body())
        }
    }

    private val _ticketspareData = SingleLiveEvent<IndentSpareDetails>()
    val SpareData: LiveData<IndentSpareDetails>
        get() = _ticketspareData

    suspend fun getsparedetailslist(ticketId : Int, authorization: String, Customer_Id : String, Tenant_Id  : Int) {
        val result = MaintenanceBaseurl.getIndentDetails(ticketId, authorization, Customer_Id, Tenant_Id)
        if (result.isSuccessful && result.body() != null) {
            _ticketspareData.postValue(result.body())
        }
    }

    private val _ticketformData = SingleLiveEvent<MaintenanceResponse>()
    val TicketFormData: LiveData<MaintenanceResponse>
        get() = _ticketformData

    suspend fun getticketformlist(type : String,category : String, authorization: String, Customer_Id : String, Tenant_Id  : Int) {
        val result = MaintenanceBaseurl.getreasons(type,category, authorization, Customer_Id, Tenant_Id)
        if (result.isSuccessful && result.body() != null) {
            _ticketformData.postValue(result.body())
        }
    }

    private val _ticketReasonFormData = SingleLiveEvent<MaintenanceResponse>()
    val TicketReasonFormData: LiveData<MaintenanceResponse>
        get() = _ticketReasonFormData

    suspend fun getticketReasonFormlist(type : String,category : String, authorization: String, Customer_Id : String, Tenant_Id  : Int) {
        val result = MaintenanceBaseurl.getreasons(type,category, authorization, Customer_Id, Tenant_Id)
        if (result.isSuccessful && result.body() != null) {
            _ticketReasonFormData.postValue(result.body())
        }
    }


    private val _ticketcompleteData = MutableLiveData<List<TaskEntity>>()
    val TicketcompleteData: LiveData<List<TaskEntity>>
        get() = _ticketcompleteData

    suspend fun getAllTasksbyTicketId(ticketId: Int){
        val tickets = userDao.getTaskById(ticketId)
        _ticketcompleteData.postValue(tickets)
    }

    private val _tickettaskData = MutableLiveData<List<TaskEntity>>()
    val TickettaskData: LiveData<List<TaskEntity>>
        get() = _tickettaskData

    suspend fun getTaskByTicketIdTaskId(ticketId: Int, taskId: Int){
        val ticket = userDao.getTaskByTicketIdTaskId(ticketId, taskId)
        _tickettaskData.postValue(ticket)
    }

    // Function to insert a ticket
    suspend fun insertTask(ticketId: Int, ticketTaskId: Int, status: String, comment : String, isSpareIndentUsed: String, fixedtype : String, fixedReason : String, openReason: String, dependency : String, description : String) {
        val ticket = TaskEntity(ticketId, ticketTaskId, status,comment,isSpareIndentUsed,fixedtype,fixedReason,openReason,dependency,description)
        userDao.insertTask(ticket)
    }


    suspend fun deleteTask(ticketId: Int) {
        userDao.deleteTask(ticketId)
    }


    private val _frreportData = SingleLiveEvent<MaintenancefrreportsData>()
    val FrreportData: LiveData<MaintenancefrreportsData>
        get() = _frreportData

    suspend fun getreportslist(userId: Int, authorization: String, Customer_Id : String, Tenant_Id  : Int) {
        val result = MaintenanceBaseurl.getreports(userId, authorization, Customer_Id, Tenant_Id)
        if (result.isSuccessful && result.body() != null) {
            _frreportData.postValue(result.body())
        }
    }


    private val _ticketbyspare = SingleLiveEvent< List<SpareBagEntity>>()
        val TicketSpare : LiveData< List<SpareBagEntity>>
            get() = _ticketbyspare
    suspend fun getsparebyticket(ticketId: Int){
        val spares = userDao.getSpareBagsByTicketId(ticketId)
        _ticketbyspare.postValue(spares)
    }

    private val _sparebyTicketTask = SingleLiveEvent< List<SpareBagEntity>>()
    val SpareTicketTask : LiveData< List<SpareBagEntity>>
        get() = _sparebyTicketTask
    suspend fun getsparebyticketIdTaskId(ticketId: Int, taskId:Int){
        val spares = userDao.getSpareBagsByTicketIdTaskId(ticketId, taskId)
        _sparebyTicketTask.postValue(spares)
    }

    private val _ticketbyraisedindentandspare = SingleLiveEvent<List<RaiseIndentSpare>>()
    val TicketRaisedIndentandspare : LiveData<List<RaiseIndentSpare>>
        get() = _ticketbyraisedindentandspare
    suspend fun getraisedindentspares(ticketId: Int, taskId: Int){
        val indents = userDao.getrequestedIndentandSpares(ticketId, taskId)
        _ticketbyraisedindentandspare.postValue(indents)
    }

    @WorkerThread
    suspend fun insertSpareBags(spareBags: List<SpareBagEntity>) {
        userDao.insertSpareBags(spareBags)
    }

    @WorkerThread
    suspend fun deleteSpare(ticketId: Int, taskId: Int, spareId:Int) {
        userDao.deleteSpare(ticketId, taskId, spareId)
    }


    suspend fun deleteImageByTicketId(ticketId: Int) {
        userDao.deleteImagesByTicketId(ticketId)
    }


    @WorkerThread
    suspend fun deleteSpareByTicketId(ticketId: Int) {
        userDao.deleteSpareByTicketId(ticketId)
    }

    @WorkerThread
    suspend fun deleteRaiseIndentSpareByTicketId(ticketId: Int) {
        userDao.deleteRaiseIndentSpareByTicketId(ticketId)
    }


    suspend fun updateTicket(ticket: TaskEntity) {
        userDao.updateTask(ticket)
    }

    private val _raiserequest = SingleLiveEvent<RequestSpareItem>()
    val RaiseRequest: LiveData<RequestSpareItem>
        get() = _raiserequest
    suspend fun getraiserequest(authorization: String, Customer_Id: String, Tenant_Id: Int) {
        val result = MaintenanceBaseurl.getSpareRequests(authorization, Customer_Id, Tenant_Id)
        if (result.isSuccessful && result.body() != null) {
            _raiserequest.postValue(result.body()!!)
        }
    }

    suspend fun addImageToTicket(ticketId: Int, taskId: Int, imageUri: String) {
        val ticketImage = TicketImageEntity(ticketId = ticketId, taskId = taskId, imageUri = imageUri)
        userDao.insertImage(ticketImage)
    }

    private val _ticketimages = SingleLiveEvent<List<TicketImageEntity>>()
    val TicketImages : LiveData<List<TicketImageEntity>>
        get() = _ticketimages
    suspend fun getImagesForTicket(ticketId: Int, taskId: Int){
        val images = userDao.getImagesForTicket(ticketId, taskId)
        _ticketimages.postValue(images)
    }

    suspend fun updateImage(ticketId: Int, taskId: Int, imageUri: String) {
        val ticketImage = TicketImageEntity(ticketId = ticketId, taskId = taskId, imageUri = imageUri)
        userDao.updateImage(ticketImage)
    }
    @WorkerThread
    suspend fun insertRaiseIndentSparelist(raiseIndentSpare: List<RaiseIndentSpare>) {
        userDao.insertRaiseIndentSpare(raiseIndentSpare)
    }

    suspend fun getRaiseIndentSpareById(id: Int, ticketId: Int, taskId: Int): RaiseIndentSpare? {
        return userDao.getRaiseIndentSpareById(id, ticketId, taskId)
    }

    suspend fun updateRaiseIndentSpareQuantity(id: Int, ticketId: Int, taskId: Int, newQuantity: Int) {
        userDao.updateQuantity(id, ticketId, taskId, newQuantity)
    }

    suspend fun deleteRaiseIndentSpare(id: Int, ticketId: Int, taskId: Int) {
        userDao.deleteRaiseIndentSpare(id, ticketId, taskId)
    }

    private val ticket_summary = SingleLiveEvent<TicketDetailsSummaryResponse>()
    val TicketSummary: LiveData<TicketDetailsSummaryResponse>
        get() = ticket_summary

    suspend fun submitSummarydata(authorization: String, Customer_Id : String, Tenant_Id  : Int, requestBody: RequestBody) {
        val result = MaintenanceBaseurl.SubmitSummaryTicketDetails(authorization, Customer_Id, Tenant_Id, requestBody)
        if (result.isSuccessful && result.body() != null) {
            ticket_summary.postValue(result.body())
        }
    }

    suspend fun clearAllTasks() {
        userDao.clearallTasks()
    }


    suspend fun getCombinedTicketData(ticketId: Int): CombinedTicketData? {
        val ticket = userDao.getTaskById(ticketId)
        val spareBags = userDao.getSpareBagsByTicketId(ticketId)
        val raiseIndentSpares = userDao.getRaiseIndentSparesByTicketId(ticketId)

        return ticket?.let {
            CombinedTicketData(
                ticket = it,
                spareBags = spareBags,
                raiseIndentSpares = raiseIndentSpares
            )
        }
    }

}

