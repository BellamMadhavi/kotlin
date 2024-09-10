package com.qcauditapp.di

import androidx.lifecycle.ViewModel
import com.ivis.qcauditapp.viewmodels.FRreportViewModel
import com.ivis.qcauditapp.viewmodels.MainViewModel2
import com.ivis.qcauditapp.viewmodels.MaintenanceResponseViewModel
import com.ivis.qcauditapp.viewmodels.maintenance.RaiseRequestViewModel
import com.ivis.qcauditapp.viewmodels.maintenance.SubmitSummaryViewModel
import com.ivis.qcauditapp.viewmodels.maintenance.TicketTaskDbDataViewModel
import com.ivis.qcauditapp.viewmodels.maintenance.indent.SpareViewModel
import com.ivis.qcauditapp.viewmodels.maintenance.tickets.SchedulesViewModel
import com.ivis.qcauditapp.viewmodels.maintenance.tickets.TicketDetailsViewModel
import com.qcauditapp.viewmodels.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @ClassKey(MainViewModel::class)
    @IntoMap
    abstract fun mainViewModel(mainViewModel: MainViewModel) : ViewModel

    @Binds
    @ClassKey(MainViewModel2::class)
    @IntoMap
    abstract fun mainViewModel2(mainViewModel2: MainViewModel2) : ViewModel

    @Binds
    @ClassKey(SchedulesViewModel::class)
    @IntoMap
    abstract fun SchedulesViewModel(schedulesViewModel: SchedulesViewModel) : ViewModel

    @Binds
    @ClassKey(TicketDetailsViewModel::class)
    @IntoMap
    abstract fun TicketDetailsViewModel(ticketDetailsViewModel: TicketDetailsViewModel) : ViewModel

    @Binds
    @ClassKey(SpareViewModel::class)
    @IntoMap
    abstract fun SpareViewModel(spareViewModel: SpareViewModel) : ViewModel


    @Binds
    @ClassKey(MaintenanceResponseViewModel::class)
    @IntoMap
    abstract fun MaintenanceResponseViewModel(maintenanceResponseViewModel: MaintenanceResponseViewModel) : ViewModel

    @Binds
    @ClassKey(TicketTaskDbDataViewModel::class)
    @IntoMap
    abstract fun TicketTaskDbDataViewModel(ticketTaskDbDataViewModel: TicketTaskDbDataViewModel) : ViewModel

    @Binds
    @ClassKey(FRreportViewModel::class)
    @IntoMap
    abstract fun FRreportViewModel(fRreportViewModel: FRreportViewModel) : ViewModel

    @Binds
    @ClassKey(RaiseRequestViewModel::class)
    @IntoMap
    abstract fun RaiseRequestViewModel(raiseRequestViewModel: RaiseRequestViewModel) : ViewModel


    @Binds
    @ClassKey(SubmitSummaryViewModel::class)
    @IntoMap
    abstract fun SubmitSummaryViewModel(submitSummaryViewModel: SubmitSummaryViewModel) : ViewModel

}