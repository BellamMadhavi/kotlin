package com.ivis.qcauditapp.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ivis.qcauditapp.utils.QcPreferences
import com.ivis.qcauditapp.viewmodels.maintenance.tickets.TicketDetailsViewModel
import com.qcauditapp.ApplicationController
import com.qcauditapp.R
import com.qcauditapp.viewmodels.MainViewModelFactory
import javax.inject.Inject
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.adapter.maintenance.tasks.TaskListAdapter
import com.ivis.qcauditapp.db.TaskEntity
import com.ivis.qcauditapp.models.IndentItem
import com.ivis.qcauditapp.models.SpareBagItem
import com.ivis.qcauditapp.models.TicketTask
import com.ivis.qcauditapp.retrofit.ItemOnClickListener
import com.ivis.qcauditapp.viewmodels.maintenance.TicketTaskDbDataViewModel
import kotlinx.coroutines.launch

class MaintenanceTaskListFragment : Fragment(), ItemOnClickListener {
    private lateinit var serverTasks: MutableList<TicketTask>
    private  var dbTasksCount = 0

    lateinit var ticketDetailsViewModel: TicketDetailsViewModel
    lateinit var ticketTaskDbDataViewModel: TicketTaskDbDataViewModel

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    lateinit var taskListAdapter: TaskListAdapter
    lateinit var rcv_tasklist: RecyclerView
    lateinit var continue_btn: Button
    lateinit var txt_ticket_name: TextView
    lateinit var img_helpdesk_back: ImageView
    lateinit var logout_img: ImageView
    lateinit var txt_ticket_id: TextView
    lateinit var cancel: Button
    lateinit var site_name: TextView

    private var ticketAuditId: Int? = null

    private  var selectedTaskStatus : String = ""
    private var ticketId : Int? = null

    lateinit var myPreferences: QcPreferences

    private var mDialog: Dialog? = null

    val tasklist: MutableList<TicketTask> = mutableListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_maintenance_task_list, container, false)

        myPreferences = QcPreferences(requireContext())

        mDialog =
            (requireActivity().application as ApplicationController).showLoadingDialog(requireContext(), requireActivity().resources.getString(R.string.loading))
        mDialog!!.show()

        rcv_tasklist = view.findViewById(R.id.rcv_tasklist)
        continue_btn = view.findViewById(R.id.continue_btn)
        txt_ticket_name = view.findViewById(R.id.txt_ticket_name)
        img_helpdesk_back = view.findViewById(R.id.img_helpdesk_back)
        logout_img = view.findViewById(R.id.logout_img)
        site_name = view.findViewById(R.id.site_name)
        txt_ticket_id = view.findViewById(R.id.txt_ticket_id)
        cancel = view.findViewById(R.id.btn_cancel)

        (requireActivity().application as ApplicationController).applicationComponent.inject(this)
        ticketDetailsViewModel = ViewModelProvider(this, mainViewModelFactory).get(TicketDetailsViewModel::class.java)
        ticketTaskDbDataViewModel = ViewModelProvider(this, mainViewModelFactory).get(TicketTaskDbDataViewModel::class.java)

        var access_token = myPreferences.getString("accessToken", "")
        var Customer_Id = myPreferences.getString("Customer-Id", "")
        site_name.text = "You are in"+" "+myPreferences.getString("sitename", "")
        var Tenant_Id = myPreferences.getInt("Tenant-Id", 0)
        var userId = myPreferences.getInt("userId", 0)
        ticketId = myPreferences.getInt("TicketId", 0)
        txt_ticket_id.text = ticketId.toString()
        ticketAuditId = myPreferences.getInt("TicketAuditId", 0)

        ticketTaskDbDataViewModel.getTicketsByTicketId(ticketId!!)

        // start
        // Fetch task details from the server
        ticketDetailsViewModel.fetchTicketDetailsdata(ticketId, "Bearer $access_token", "null", Tenant_Id)
        ticketDetailsViewModel.ticketdetailsdatalist.removeObservers(viewLifecycleOwner)
        ticketDetailsViewModel.ticketdetailsdatalist.observe(viewLifecycleOwner, Observer { serverResponse ->
            serverResponse?.let {
                if (it.errorCode?.toInt() == 200) {

                    // Step 2: Retrieve the tasks from the server response
                    serverTasks = it.results.ticketTask.toMutableList()
                    txt_ticket_name.text = it.results.ticketInfo.title
                    myPreferences.saveString("title", it.results.ticketInfo.title)

                    // Step 3: Fetch tasks from the database
                    ticketTaskDbDataViewModel.getTicketsByTicketId(ticketId!!)
                    ticketTaskDbDataViewModel.tickettaskData.removeObservers(viewLifecycleOwner)
                    ticketTaskDbDataViewModel.tickettaskData.observe(viewLifecycleOwner, Observer { dbTasks ->
                        if (dbTasks != null && dbTasks.isNotEmpty()) {
                            dbTasksCount = dbTasks.size

                            // Replace server tasks with merged tasks if they exist
                            dbTasks.forEach { dbTask ->
                                val index = serverTasks.indexOfFirst { it.taskId == dbTask.ticketTaskId }
                                if (index != -1) {
                                    serverTasks[index] = mergeEntityWithServerTask(dbTask, serverTasks[index])
                                }
                            }
                            if(serverTasks.size == 1) {
                                selectedTaskStatus = serverTasks[0].status

                                myPreferences.saveString("Status", selectedTaskStatus)
                                myPreferences.saveInt("taskId", serverTasks[0].taskId)
                                myPreferences.saveString("selected_taskname", serverTasks[0].title)
                                myPreferences.saveBoolean("lastTask", true)
                            }
                        }

                        // Extract the list of updated task IDs
                        val updatedTaskIds = dbTasks.map { it.ticketTaskId }
                        val attended:String = myPreferences.getString("attended", "Pending")

                        // Step 4: Update the adapter with the final task list and list of updated task IDs
                        taskListAdapter = TaskListAdapter(requireContext(), serverTasks, this, updatedTaskIds, attended)
                        rcv_tasklist.layoutManager = LinearLayoutManager(requireContext())
                        rcv_tasklist.adapter = taskListAdapter
                        rcv_tasklist?.adapter?.notifyDataSetChanged()

                        if (mDialog!!.isShowing) {
                            mDialog!!.dismiss()
                            mDialog!!.hide()
                        }
                    })
                } else {
                    if (mDialog!!.isShowing) {
                        mDialog!!.dismiss()
                        mDialog!!.hide()
                    }
                    // Handle the error case
                }
            }
        })

        // end


        continue_btn.setOnClickListener {
            if (!selectedTaskStatus.equals("Fixed", ignoreCase = true) &&
                !selectedTaskStatus.equals("Open", ignoreCase = true)) {
                Toast.makeText(context, "Select atleast one task to continue", Toast.LENGTH_SHORT).show()
            } else {
                val status = myPreferences.getString("Status")
                val taskId = myPreferences.getInt("taskId")
                val taskName = myPreferences.getString("selected_taskname")

                val allOtherTasksCompleted = serverTasks.filter { it.taskId != taskId }.all { item ->
                    (item.fixedReason?.isNotBlank() == true || item.dependency?.isNotBlank() == true) && item.remarks?.isNotBlank() == true
                }
                myPreferences.saveBoolean("lastTask", allOtherTasksCompleted)

                // Check if the ticket already exists
                lifecycleScope.launch {
                    val existingTickEntry = ticketTaskDbDataViewModel.getTaskByTicketIdTaskId(ticketId!!, taskId)

                    ticketTaskDbDataViewModel.taskData.removeObservers(requireActivity())
                    ticketTaskDbDataViewModel.taskData.observe(viewLifecycleOwner, Observer { ticket ->
                        ticket?.let {
                            if (it.isEmpty()) {
                                // If the ticket does not exist, insert it
                                val taskEntity = TaskEntity(
                                    ticketId = ticketId!!,
                                    ticketTaskId = taskId,
                                    taskstatus = status,
                                    comments = "",
                                    isSpareIndentUsed = "",
                                    fixedtype = "",
                                    fixedReason = "",
                                    openReason = "",
                                    dependency = "",
                                    description = taskName
                                )
                                ticketTaskDbDataViewModel.insertTicket(
                                    taskEntity.ticketId,
                                    taskEntity.ticketTaskId,
                                    taskEntity.taskstatus,
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    taskName
                                )

                                lifecycleScope.launch {
                                    val existingTickEntry = ticketTaskDbDataViewModel.getTaskByTicketIdTaskId(taskEntity.ticketId!!, taskEntity.ticketTaskId)
                                    ticketTaskDbDataViewModel.taskData.removeObservers(requireActivity())
                                    ticketTaskDbDataViewModel.taskData.observe(viewLifecycleOwner, Observer { ticket ->
                                           ticket?.let {
                                            if (it.isNotEmpty()) {
                                                Toast.makeText(requireContext(), "retrieved taskName = " + ticket.get(0).description, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    })
                                }
                            } else {
                                // If the ticket does not exist, insert it
                                val taskEntity = TaskEntity(
                                    ticketId = ticketId!!,
                                    ticketTaskId = taskId,
                                    taskstatus = status,
                                    comments = ticket[0].comments,
                                    isSpareIndentUsed = ticket[0].isSpareIndentUsed,
                                    fixedtype = ticket[0].fixedtype,
                                    fixedReason = ticket[0].fixedReason,
                                    openReason = ticket[0].openReason,
                                    dependency = ticket[0].dependency,
                                    description = taskName
                                )
                                ticketTaskDbDataViewModel.updateTicket(
                                    taskEntity.ticketId,
                                    taskEntity.ticketTaskId,
                                    taskEntity.taskstatus,
                                    ticket[0].comments,
                                    ticket[0].isSpareIndentUsed!!,
                                    ticket[0].fixedtype!!,
                                    ticket[0].fixedReason!!,
                                    ticket[0].openReason!!,
                                    ticket[0].dependency!!,
                                    taskName
                                )
                            }
                            findNavController().navigate(R.id.action_maintenance_task_list_fragment_to_maintenance_taskform_fragment)
                        }
                    })

                }
            }
        }
        img_helpdesk_back.setOnClickListener {
            findNavController().navigate(R.id.action_maintenance_task_list_fragment_to_maintenance_ticket_details_fragment)
        }

        cancel.setOnClickListener {
            findNavController().navigate(R.id.action_maintenance_task_list_fragment_to_maintenance_ticket_details_fragment)
        }

        logout_img.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val builder = AlertDialog.Builder(requireContext(),R.style.CustomAlertDialogTheme)
                builder.setTitle("Logout Confirmation")
                builder.setMessage("Are you sure you want to log out?")
                builder.setPositiveButton("Yes") { dialog, which ->
                    val myPreferences = QcPreferences(requireContext())
                    myPreferences.clearPreferences()
                    ticketTaskDbDataViewModel.clearAllTickets()
                    findNavController().navigate(R.id.action_maintenance_task_list_fragment_to_login_fragment)
                }
                builder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.setCancelable(false)
                dialog.show()
            }
        })

        return view
    }

    fun mergeEntityWithServerTask(dbTask: TaskEntity, serverTask: TicketTask): TicketTask {
        val ticketTask = TicketTask(
            fixedType = dbTask.fixedtype ?: serverTask.fixedType ?: "",
            closedBy = serverTask.closedBy.ifBlank { "" },  // Always use the server data for this field
            fixedDate = serverTask.fixedDate ?: "",  // Always use the server data for this field
            dependency = dbTask.dependency ?: serverTask.dependency ?: "",
            footageFrom = serverTask.footageFrom ?: "",  // Always use the server data for this field
            queue = serverTask.queue?:"",  // Always use the server data for this field
            openedBy = serverTask.openedBy?:"",  // Always use the server data for this field
            cameraId = serverTask.cameraId ?: "",  // Always use the server data for this field
            closedDate = serverTask.closedDate ?: "",  // Always use the server data for this field
            title = serverTask.title ,
            createdTime = serverTask.createdTime,  // Always use the server data for this field
            remarks = dbTask.comments.ifBlank { serverTask.remarks ?: "" },  // Always use the server data for this field
            openReason = dbTask.openReason.ifBlank { serverTask.openReason ?: "" },
            fixedBy = serverTask.fixedBy ?: "",  // Always use the server data for this field
            fixedReason = dbTask.fixedReason ?: serverTask.fixedReason ?: "",
            closingReason = serverTask.closingReason ?: "",  // Always use the server data for this field
            openDate = serverTask.openDate ?: "",  // Always use the server data for this field
            taskId = dbTask.ticketTaskId,
            status = dbTask.taskstatus,
            footageTo = serverTask.footageTo ?: "",  // Always use the server data for this field
            taskCategory = serverTask.taskCategory ?: ""  // Always use the server data for this field
        )
        return ticketTask
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing
                findNavController().navigate(R.id.action_maintenance_task_list_fragment_to_maintenance_ticket_details_fragment)

            }
        })

    }

    override fun MaintanenceTaskClickListener(position: Int) {
        TODO("Not yet implemented")
    }

    override fun MaintanenceTicketsClickListener(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onIndentitemClick(indentItem: IndentItem, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onSpareitemClick(spareitem: SpareBagItem, isSelected: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onRadioButtonClicked(position: Int, selectedOption: String, taskitem: TicketTask) {
            selectedTaskStatus  = selectedOption
            myPreferences.saveString("Status", selectedTaskStatus)
            myPreferences.saveInt("taskId", taskitem.taskId)
            myPreferences.saveString("selected_taskname", taskitem.title)
    }

}
