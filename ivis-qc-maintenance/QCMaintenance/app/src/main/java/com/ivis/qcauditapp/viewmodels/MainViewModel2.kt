package com.ivis.qcauditapp.viewmodels

import androidx.lifecycle.ViewModel
import com.qcauditapp.viewmodels.MainViewModel
import javax.inject.Inject

class MainViewModel2 @Inject constructor(private  val randomize: MainViewModel.Randomize) : ViewModel() {

    init {
        randomize.doAction()
    }
}