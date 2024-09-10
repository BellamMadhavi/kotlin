package com.ivis.qcauditapp.viewmodels.maintenance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivis.qcauditapp.db.RaiseIndentSpare
import com.ivis.qcauditapp.db.SpareBagEntity
import com.ivis.qcauditapp.db.TaskEntity
import com.ivis.qcauditapp.db.TicketImageEntity
import com.ivis.qcauditapp.models.CombinedTicketData
import com.qcauditapp.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TicketTaskDbDataViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
     val tickettaskData: LiveData<List<TaskEntity>>
        get() = repository.TicketcompleteData
    fun getTicketsByTicketId(ticketId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTasksbyTicketId(ticketId)
        }
    }

    fun insertTicket(ticketId: Int, ticketTaskId: Int, status: String,comment : String,isSpareIndentUsed : String,fixedtype : String,fixedReason : String,openReason: String, dependency : String, description : String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTask(ticketId, ticketTaskId, status,comment,isSpareIndentUsed,fixedtype,fixedReason,openReason,dependency,description)
        }
    }

    val ticketbytaskSpareData: LiveData<List<SpareBagEntity>>
        get() = repository.SpareTicketTask
    fun getSpareBagsByTicketIdTaskId(ticketId: Int, taskId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getsparebyticketIdTaskId(ticketId, taskId)
        }
    }


    val spareDatabyTicketId: LiveData<List<SpareBagEntity>>
        get() = repository.TicketSpare
    fun getSpareBagsByTicketId(ticketId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getsparebyticket(ticketId)
        }
    }

    fun insertSpareBags(spareBags: List<SpareBagEntity>) {
        viewModelScope.launch {
            repository.insertSpareBags(spareBags)
        }
    }

    fun deleteSpare(ticketId: Int, taskId: Int, spareId:Int) {
        viewModelScope.launch {
            repository.deleteSpare(ticketId, taskId, spareId)
        }
    }


    fun deleteTicket(ticketId: Int) {
        viewModelScope.launch {
            repository.deleteTask(ticketId)
        }
    }

    fun deleteImageByTicketId(ticketId: Int) {
        viewModelScope.launch {
            repository.deleteImageByTicketId(ticketId)
        }
    }

    fun deleteSpareByTicketId(ticketId: Int) {
        viewModelScope.launch {
            repository.deleteSpareByTicketId(ticketId)
        }
    }


    fun deleteRaiseIndentSpareByTicketId(ticketId: Int) {
        viewModelScope.launch {
            repository.deleteRaiseIndentSpareByTicketId(ticketId)
        }
    }

    fun updateTicket(ticketId: Int, taskId: Int, status: String, comments: String,isSpareIndentUsed : String, fixedType: String, fixedReason: String, openReason: String, dependency: String, description : String) {
        val ticket = TaskEntity(ticketId, taskId, status, comments,isSpareIndentUsed, fixedType, fixedReason, openReason, dependency,description)
        viewModelScope.launch {
            repository.updateTicket(ticket)
        }
    }

    fun addImageToTickets(ticketId: Int, taskId: Int, imageUri: String) {
        viewModelScope.launch {
            repository.addImageToTicket(ticketId, taskId, imageUri)
        }
    }

    val ticketbyImages: LiveData<List<TicketImageEntity>>
        get() = repository.TicketImages

    fun getImagesForTicketTask(ticketId: Int, taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getImagesForTicket(ticketId, taskId)
        }
    }

    suspend fun updateImage(ticketId: Int, taskId: Int, imageUri: String) {
        viewModelScope.launch {
            repository.updateImage(ticketId, taskId,imageUri)
        }
    }

    suspend fun insertRaiseIndentSpare(raiseIndentSpare: List<RaiseIndentSpare>) {
        viewModelScope.launch {
            repository.insertRaiseIndentSparelist(raiseIndentSpare)
        }
    }

    val ticketbyRequestedIndentandSpare: LiveData<List<RaiseIndentSpare>>
        get() = repository.TicketRaisedIndentandspare

    fun getraisedindentsandsparesbyticket(ticketId: Int, taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getraisedindentspares(ticketId, taskId)
        }
    }

    // Method to get RaiseIndentSpare by ID
    suspend fun getRaiseIndentSpareById(id: Int, ticketId: Int, taskId: Int): RaiseIndentSpare? {
        return repository.getRaiseIndentSpareById(id, ticketId, taskId)
    }

    fun updateRaiseIndentSpareQuantity(id: Int, ticketId: Int, taskId: Int, newQuantity: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            // Assuming you have a DAO method to update the quantity
            repository.updateRaiseIndentSpareQuantity(id, ticketId, taskId, newQuantity)
        }
    }

    fun deleteRaiseIndentSpare(id: Int, ticketId: Int, taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRaiseIndentSpare(id, ticketId, taskId)
        }
    }

    fun clearAllTickets() {
        viewModelScope.launch {
            repository.clearAllTasks()
        }
    }


    val taskData: LiveData<List<TaskEntity>>
        get() = repository.TickettaskData

    fun getTaskByTicketIdTaskId(ticketId: Int, taskId: Int) {
        viewModelScope.launch {
            repository.getTaskByTicketIdTaskId(ticketId, taskId)
        }
    }

    fun getCombinedTicketData(ticketId: Int): LiveData<CombinedTicketData?> {
        val liveData = MutableLiveData<CombinedTicketData?>()
        viewModelScope.launch(Dispatchers.IO) {
            val combinedData = repository.getCombinedTicketData(ticketId)
            liveData.postValue(combinedData)
        }
        return liveData
    }
}