package com.qcauditapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivis.qcauditapp.models.ApiResponse
import com.ivis.qcauditapp.models.User
import com.qcauditapp.repository.Repository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val accountdata: LiveData<ApiResponse>
        get() = repository.accountdata

    val errordata: LiveData<String>
        get() = repository.errordata
    fun fetch_login_data(email:String,pwd:String) {
        viewModelScope.launch {
            repository.getlogin(email,pwd)
        }
    }

    class Randomize @Inject constructor() {
        fun doAction() {
            Log.d("CHEEZYCODE", "Random Action")
        }
    }
}
