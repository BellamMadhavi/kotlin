package com.qcauditapp.di

import android.content.Context
import com.ivis.qcauditapp.activities.MainActivity
import com.ivis.qcauditapp.fragment.DashBoardFragment
import com.ivis.qcauditapp.fragment.LoginFragment
import com.ivis.qcauditapp.fragment.MaintenanceSummaryFragment
import com.ivis.qcauditapp.fragment.MaintenanceTaskFormFragment
import com.ivis.qcauditapp.fragment.MaintenanceTaskListFragment
import com.ivis.qcauditapp.fragment.MaintenanceTicketDetailsFragment
import com.ivis.qcauditapp.fragment.MaintenanceTicketsFragment
import com.ivis.qcauditapp.fragment.RaiseRequestFragment
import com.ivis.qcauditapp.fragment.SparsDetailsFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(loginFragment: LoginFragment)
    fun inject(dashBoardFragment: DashBoardFragment)
    fun inject(ticketsFragment: MaintenanceTicketsFragment)
    fun inject(detailsFragment: MaintenanceTicketDetailsFragment)
    fun inject(taskFragment: MaintenanceTaskListFragment)
    fun inject(sparsDetailsFragment: SparsDetailsFragment)
    fun inject(taskFormFragment: MaintenanceTaskFormFragment)
    fun inject(summaryFragment: MaintenanceSummaryFragment)
    fun inject(raiseRequestFragment: RaiseRequestFragment)


    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context) : ApplicationComponent
    }
}