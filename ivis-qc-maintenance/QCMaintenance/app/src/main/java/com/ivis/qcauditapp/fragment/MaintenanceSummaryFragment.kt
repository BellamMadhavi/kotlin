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
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.adapter.maintenance.TicketdetailsTaskAdapter
import com.ivis.qcauditapp.adapter.maintenance.indent.RaiseIndentSpareAdapterSummary
import com.ivis.qcauditapp.adapter.maintenance.indent.SpareAdapterSummary
import com.ivis.qcauditapp.db.RaiseIndentSpare
import com.ivis.qcauditapp.db.SpareBagEntity
import com.ivis.qcauditapp.db.TaskEntity
import com.ivis.qcauditapp.models.IndentRequest
import com.ivis.qcauditapp.models.SpareIndent
import com.ivis.qcauditapp.models.SummaryTask
import com.ivis.qcauditapp.models.Task
import com.ivis.qcauditapp.models.TicketDetailsSummary
import com.ivis.qcauditapp.models.TicketTask
import com.ivis.qcauditapp.utils.QcPreferences
import com.ivis.qcauditapp.viewmodels.maintenance.SubmitSummaryViewModel
import com.ivis.qcauditapp.viewmodels.maintenance.TicketTaskDbDataViewModel
import com.ivis.qcauditapp.viewmodels.maintenance.tickets.TicketDetailsViewModel
import com.qcauditapp.ApplicationController
import com.qcauditapp.R
import com.qcauditapp.viewmodels.MainViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class MaintenanceSummaryFragment  : Fragment()  {
    private var inCompleteTaskFound: Boolean= false
    private var inCompleteTaskName:String=""
    lateinit var ticketDetailsViewModel: TicketDetailsViewModel
    lateinit var ticketTaskDbDataViewModel: TicketTaskDbDataViewModel
    lateinit var submitSummaryViewModel: SubmitSummaryViewModel
    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    lateinit var myPreferences: QcPreferences

    lateinit var ticketdetailsSummary : TicketDetailsSummary
// start
    lateinit var ticketdetails_taskinfo_rcv: RecyclerView
    lateinit var ticket_rel_layout: RelativeLayout
    lateinit var ticket_layout: LinearLayout

    lateinit var task_rel_layout: RelativeLayout

    private lateinit var raised_indentandspare_details_rcv: RecyclerView
    lateinit var requested_indentandspare_layout: LinearLayout
    lateinit var requested_indentandspare_rel_layout: RelativeLayout

    lateinit var indentandspare_layout: LinearLayout
    lateinit var indentandspare_rel_layout: RelativeLayout

    private var istaskInfoOpen = false



    private var isGeneralInfoOpen = false
    private var reqIndentOpen = false
    private var indentsparesInfoOpen = false
// end

    private lateinit var state : String
    private lateinit var title : String
    private var ticketId : Int? = null
    private var taskId : Int? = null
    private var ticketAuditId: Int? = null
    lateinit var site_name: TextView

    private val summaryTasksList = mutableListOf<Task>()
    private lateinit var spareAdapterSummary: SpareAdapterSummary
    private lateinit var raiseIndentSpareAdapterSummary: RaiseIndentSpareAdapterSummary

    private lateinit var logout_img: ImageView
    private lateinit var img_helpdesk_back: ImageView
    private lateinit var taskid_txt: TextView
    private lateinit var txt_ticketid: TextView
    private lateinit var txt_status: TextView
    private lateinit var comments_summary: TextView
    private lateinit var txt_type_summary: TextView
    private lateinit var txt_reason_summary: TextView
    private lateinit var spare_items_rcv: RecyclerView
    private lateinit var images_rcv: RecyclerView
    private lateinit var txt_ticket_id: TextView
    private lateinit var txt_ticket_name: TextView
    private lateinit var btn_cancel: Button
    private lateinit var sparebag_layout: LinearLayout
    private lateinit var btn_continue: Button
    private lateinit var image_layout: LinearLayout
    private val RaisedIndentandSpareList = mutableListOf<RaiseIndentSpare>()
    private val spareList = mutableListOf<SpareBagEntity>()

    private var mDialog: Dialog? = null
    lateinit var txt_ticketinfo_title: TextView
    lateinit var txt_ticketinfo_ticketType: TextView
    lateinit var txt_ticketinfo_priority: TextView
    lateinit var txt_ticketInfo_teamlead: TextView
    lateinit var txt_ticketInfo_ticketTargetDate: TextView
    lateinit var txt_ticketType_siteReady: TextView
    lateinit var txt_ticketType_sitedownType: TextView
    lateinit var txt_ticketType_sitename: TextView
    lateinit var txt_ticketType_assignedto: TextView
    lateinit var txt_ticketType_status: TextView
    lateinit var txt_ticketType_fieldRep: TextView
    lateinit var txt_ticketType_spareReached: TextView
    lateinit var txt_ticketType_ticketQueue: TextView
    lateinit var txt_ticketType_creationType: TextView


    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maintenance_summary, container, false)

        myPreferences = QcPreferences(requireContext())

        // Initialize Views
        logout_img = view.findViewById(R.id.logout_img)
        sparebag_layout = view.findViewById(R.id.sparebag_layout)
        image_layout = view.findViewById(R.id.image_layout)
        img_helpdesk_back = view.findViewById(R.id.img_helpdesk_back)
        images_rcv = view.findViewById(R.id.images_rcv)

        spare_items_rcv = view.findViewById(R.id.spare_items_rcv)
        txt_ticket_id = view.findViewById(R.id.txt_ticket_id)
        txt_ticketid = view.findViewById(R.id.txt_ticketinfo_title)
        txt_ticket_name = view.findViewById(R.id.txt_ticket_name)

        site_name = view.findViewById(R.id.site_name)
        btn_cancel = view.findViewById(R.id.btn_cancel)
        btn_continue = view.findViewById(R.id.btn_continue)

        // start
        ticketdetails_taskinfo_rcv  = view.findViewById(R.id.ticketdetails_taskinfo_rcv)

        ticketdetails_taskinfo_rcv.visibility = View.GONE



        ticket_layout = view.findViewById(R.id.ticket_info_layout)
        ticket_rel_layout = view.findViewById(R.id.ticket_rel_layout)
        task_rel_layout = view.findViewById(R.id.task_rel_layout)


        raised_indentandspare_details_rcv = view.findViewById(R.id.raised_indentandspare_details_rcv)
        raised_indentandspare_details_rcv.visibility = View.GONE
        requested_indentandspare_layout = view.findViewById(R.id.requested_indentandspare_layout)
        requested_indentandspare_rel_layout = view.findViewById(R.id.requested_indentandspare_relative_layout)

        indentandspare_layout = view.findViewById(R.id.sparebag_layout)
        indentandspare_rel_layout = view.findViewById(R.id.indents_spares_used_rel_layout)

        // end
        site_name.text = "You are in " + myPreferences.getString("sitename", "")

        // Dependency Injection
        (requireActivity().application as ApplicationController).applicationComponent.inject(this)
        ticketDetailsViewModel = ViewModelProvider(this, mainViewModelFactory).get(TicketDetailsViewModel::class.java)
        ticketTaskDbDataViewModel = ViewModelProvider(this, mainViewModelFactory).get(TicketTaskDbDataViewModel::class.java)
        submitSummaryViewModel = ViewModelProvider(this, mainViewModelFactory).get(SubmitSummaryViewModel::class.java)


        // Retrieve Preferences
        val access_token = myPreferences.getString("accessToken", "")
        val Tenant_Id = myPreferences.getInt("Tenant-Id", 0)
        ticketId = myPreferences.getInt("TicketId", 0)
        taskId = myPreferences.getInt("taskId", 0)
        ticketAuditId = myPreferences.getInt("TicketAuditId", 0)
        state = myPreferences.getString("Status", "")
        title = myPreferences.getString("TicketName", "")

        txt_ticket_id.text = ticketId.toString()
        txt_ticket_name.text = title
        txt_ticketid.text = title



        txt_ticketinfo_title = view.findViewById(R.id.txt_ticketinfo_title)
        txt_ticketinfo_ticketType = view.findViewById(R.id.txt_ticketinfo_ticketType)
        txt_ticketinfo_priority = view.findViewById(R.id.txt_ticketinfo_priority)
        txt_ticketInfo_teamlead = view.findViewById(R.id.txt_ticketInfo_teamlead)
        txt_ticketInfo_ticketTargetDate = view.findViewById(R.id.txt_ticketInfo_ticketTargetDate)
        txt_ticketType_siteReady  = view.findViewById(R.id.txt_ticketType_siteReady)
        txt_ticketType_sitedownType  = view.findViewById(R.id.txt_ticketType_sitedownType)
        txt_ticketType_sitename  = view.findViewById(R.id.txt_ticketType_sitename)
        txt_ticketType_assignedto  = view.findViewById(R.id.txt_ticketType_assignedto)
        txt_ticketType_status  = view.findViewById(R.id.txt_ticketType_status)
        txt_ticketType_fieldRep  = view.findViewById(R.id.txt_ticketType_fieldRep)
        txt_ticketType_spareReached  = view.findViewById(R.id.txt_ticketType_spareReached)
        txt_ticketType_ticketQueue  = view.findViewById(R.id.txt_ticketType_ticketQueue)
        txt_ticketType_creationType  = view.findViewById(R.id.txt_ticketType_creationType)

        mDialog =
            (requireActivity().application as ApplicationController).showLoadingDialog(requireContext(), requireActivity().resources.getString(R.string.loading))
        mDialog!!.show()
        ticketDetailsViewModel.fetchTicketDetailsdata(ticketId, "Bearer $access_token", "null", Tenant_Id)
        ticketDetailsViewModel.ticketdetailsdatalist.removeObservers(requireActivity())
        ticketDetailsViewModel.ticketdetailsdatalist.observe(requireActivity(), Observer { responseList ->
            // Check if the list is not null and not empty
            if (responseList != null && responseList.errorCode.toInt() == 200) {
                if (mDialog!!.isShowing && mDialog != null) {
                    mDialog!!.dismiss()
                    mDialog!!.hide()
                }
                txt_ticketinfo_title.text = responseList.results.ticketInfo.title
                txt_ticketinfo_ticketType.text = responseList.results.ticketInfo.ticketType
                txt_ticketinfo_priority.text = responseList.results.ticketInfo.priority
                txt_ticketInfo_teamlead.text = responseList.results.ticketInfo.teamLead
                txt_ticketInfo_ticketTargetDate.text = responseList.results.ticketInfo.targetDate
                txt_ticketType_siteReady.text = responseList.results.ticketInfo.siteReady
                txt_ticketType_sitedownType.text = responseList.results.ticketInfo.siteDownType
                txt_ticketType_sitename.text = responseList.results.ticketInfo.siteName
                txt_ticketType_assignedto.text = responseList.results.ticketInfo.assignedTo
                txt_ticketType_status.text = responseList.results.ticketInfo.status
                txt_ticketType_fieldRep.text = responseList.results.ticketInfo.fieldRep
                txt_ticketType_spareReached.text = responseList.results.ticketInfo.spareReached
                txt_ticketType_ticketQueue.text = responseList.results.ticketInfo.queue
                txt_ticketType_creationType.text = responseList.results.ticketInfo.creationType

            }
        })

                initializeTicketDetailsSummary()
        // Observe Data
//        observeTicketData()
//        observeSpareData()
//        observeRaiseIndentAndSpareData()

        observeCombinedTicketData()
        // Handle Click Events
        setupClickListeners()

        btn_continue.setOnClickListener{
            if(inCompleteTaskFound) {
                AlertDialog.Builder(context,R.style.CustomAlertDialogTheme)
                    .setTitle("Incomplete Task")
                    .setMessage("Task '${inCompleteTaskName}' has not been updated with either an open reason or a fixed reason.")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }else {
                val requestBody = createJsonRequestBody()
                submitSummaryViewModel.submitSummarydata("Bearer $access_token", "null", Tenant_Id,requestBody)
            }
        }

        submitSummaryViewModel.submitTicketdata.removeObservers(requireActivity())
        submitSummaryViewModel.submitTicketdata.observe(viewLifecycleOwner, Observer { data ->
            data?.let {
                val errorCode = it.errorCode
                val message = if (errorCode == "200") it.results else it.errorMessage


                if (errorCode == "200") {
                    // Ticket details successfully submitted, proceed to delete local data
                    deleteTicketDetailsFromLocalDb()
                }

                val dialogView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_custom_alert, null)
                val icon = dialogView.findViewById<ImageView>(R.id.icon)
                val title = dialogView.findViewById<TextView>(R.id.title)
                val messageTextView = dialogView.findViewById<TextView>(R.id.message)

                if (errorCode == "200") {
                    icon.setImageResource(R.drawable.ic_success) // Set your success icon here
                    title.text = "Success"
                    title.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.holo_green_dark
                        )
                    )
                } else {
                    icon.setImageResource(R.drawable.ic_failure) // Set your failure icon here
                    title.text = "Error"
                    title.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.holo_red_dark
                        )
                    )
                }

                messageTextView.text = message

                val alertDialog = AlertDialog.Builder(requireContext(),R.style.CustomAlertDialogTheme)
                    .setView(dialogView)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        findNavController().navigate(R.id.action_summary_fragment_to_ticket_list_fragment)
                    }
                    .create()

                alertDialog.show()
            } ?: run {
                val dialogView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_custom_alert, null)
                val icon = dialogView.findViewById<ImageView>(R.id.icon)
                val title = dialogView.findViewById<TextView>(R.id.title)
                val messageTextView = dialogView.findViewById<TextView>(R.id.message)

                icon.setImageResource(R.drawable.ic_failure) // Set your failure icon here
                title.text = "Error"
                title.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.holo_red_dark
                    )
                )
                messageTextView.text = "Unexpected error occurred."

                val alertDialog = AlertDialog.Builder(requireContext(),R.style.CustomAlertDialogTheme)
                    .setView(dialogView)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        findNavController().navigate(R.id.action_summary_fragment_to_ticket_list_fragment)
                    }
                    .create()

                alertDialog.show()
            }
        })
        return view
    }

    private fun deleteTicketDetailsFromLocalDb() {
        ticketId?.let { ticketId ->
            if (state == "Fixed") {
                ticketTaskDbDataViewModel.deleteImageByTicketId(ticketId)
                ticketTaskDbDataViewModel.deleteSpareByTicketId(ticketId)
                ticketTaskDbDataViewModel.deleteTicket(ticketId)
            } else if (state == "Open") {
                // Delete tasks and indent requests if the ticket is open
                ticketTaskDbDataViewModel.deleteImageByTicketId(ticketId)
                ticketTaskDbDataViewModel.deleteRaiseIndentSpareByTicketId(ticketId)
                ticketTaskDbDataViewModel.deleteTicket(ticketId)
            }
        }
    }


    private fun TaskEntity.toTask(): Task {
        return Task(
            taskId = this.ticketTaskId,
            taskTitle = this.description,
            status = this.taskstatus,
            openReason = this.openReason,
            dependency = this.dependency ?: "",
            fixedType = this.fixedtype,
            fixedReason = this.fixedReason,
            remarks = this.comments,
            sparesUsed = this.isSpareIndentUsed?.toBoolean() ?: false
        )
    }

    fun TaskEntity.toTicketTask(): TicketTask {
        return TicketTask(
            fixedType = this.fixedtype ?: "",
            closedBy = "",  // Default value
            fixedDate = "",  // Default value
            dependency = this.dependency ?: "",
            footageFrom = "",  // Default value
            queue = "",  // Default value
            openedBy = "",  // Default value
            cameraId = "",  // Default value
            closedDate = "",  // Default value
            title = this.description,
            createdTime = "",  // Default value, replace with actual if available
            remarks = this.comments,
            openReason = this.openReason,
            fixedBy = "",  // Default value
            fixedReason = this.fixedReason ?: "",
            closingReason = "",  // Default value
            openDate = "",  // Default value
            taskId = this.ticketTaskId,
            status = this.taskstatus,
            footageTo = "",  // Default value
            taskCategory = ""  // Default value
        )
    }

    private fun observeCombinedTicketData() {
        ticketTaskDbDataViewModel.getCombinedTicketData(ticketId!!).observe(viewLifecycleOwner, Observer { combinedData ->
            combinedData?.let {
                // Process the ticket data
                val ticketData = it.ticket
/*                txt_ticketinfo_ticketType .text =  responseList.results.ticketInfo.ticketType
                txt_ticketinfo_priority .text = responseList.results.ticketInfo.priority
                txt_ticketInfo_teamlead .text = responseList.results.ticketInfo.teamLead
                txt_ticketInfo_ticketTargetDate.text = responseList.results.ticketInfo.targetDate
                txt_ticketType_siteReady.text = responseList.results.ticketInfo.siteReady
                txt_ticketType_sitedownType.text = responseList.results.ticketInfo.siteDownType
                txt_ticketType_sitename.text = responseList.results.ticketInfo.siteName
                txt_ticketType_assignedto.text = responseList.results.ticketInfo.assignedTo
                txt_ticketType_status.text = responseList.results.ticketInfo.status
                txt_ticketType_fieldRep.text = responseList.results.ticketInfo.fieldRep
                txt_ticketType_spareReached.text = responseList.results.ticketInfo.spareReached
                txt_ticketType_ticketQueue.text = responseList.results.ticketInfo.queue
                txt_ticketType_creationType .text = responseList.results.ticketInfo.creationType
*/
                val ticketTaskList: List<TicketTask> = it.ticket.map { it.toTicketTask() }
                val adapter = TicketdetailsTaskAdapter(ticketTaskList)
                ticketdetails_taskinfo_rcv.layoutManager = LinearLayoutManager(requireContext())
                ticketdetails_taskinfo_rcv.adapter = adapter
                summaryTasksList.clear()
                summaryTasksList.addAll(it.ticket.map { taskEntity -> taskEntity.toTask() })
                for (task in summaryTasksList) {
                    if (task.openReason.isNullOrBlank() && task.fixedReason.isNullOrBlank()) {
                        AlertDialog.Builder(context,R.style.CustomAlertDialogTheme)
                            .setTitle("Incomplete Task")
                            .setMessage("Task '${task.taskTitle}' has not been updated with either an open reason or a fixed reason.")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                        inCompleteTaskName = task.taskTitle
                        inCompleteTaskFound = true
                    }
                }
                // Process the raised indent spares dataee
                RaisedIndentandSpareList.clear()
                RaisedIndentandSpareList.addAll(it.raiseIndentSpares)
                raiseIndentSpareAdapterSummary = RaiseIndentSpareAdapterSummary(requireContext(), RaisedIndentandSpareList)
                raised_indentandspare_details_rcv.layoutManager = LinearLayoutManager(requireContext())
                raised_indentandspare_details_rcv.adapter = raiseIndentSpareAdapterSummary
                raiseIndentSpareAdapterSummary.notifyDataSetChanged()

                // Assuming `it.spareBags` is a List<SpareBagEntity>
                val ticketTaskSpareindentinfo: List<SpareIndent> = it.spareBags.map { spareBagEntity ->
                    SpareIndent(
                        indentId = spareBagEntity.id,
                        ticketId = spareBagEntity.ticketId,
                        unitId = "", // You may need to provide a unitId based on your logic
                        indentStatus = spareBagEntity.status,
                        requestedDate = "", // You may need to provide a requestedDate based on your logic
                        dispatchedDate = spareBagEntity.dispatchedDate,
                        itemName = spareBagEntity.itemDescription,
                        reqFieldRep = "", // You may need to provide reqFieldRep based on your logic
                        teamLead = "", // You may need to provide teamLead based on your logic
                        receivedDate = spareBagEntity.receivedDate
                    )
                }

                spareList.clear()
                spareList.addAll(it.spareBags)
                spareAdapterSummary = SpareAdapterSummary(requireContext(), spareList)
                spare_items_rcv.layoutManager = LinearLayoutManager(requireContext())
                spare_items_rcv.adapter = spareAdapterSummary
                spareAdapterSummary.notifyDataSetChanged()

                // Re-initialize Ticket Details Summary
                initializeTicketDetailsSummary()
            }
        })
    }

    fun SummaryTask.toTask(): Task {
        return Task(
            taskId = this.taskId,
            taskTitle = this.taskTitle,
            status = this.status,
            openReason = this.openReason,
            dependency = this.dependency,
            fixedType = this.fixedType,
            fixedReason = this.fixedReason,
            remarks = this.remarks,
            sparesUsed = this.sparesUsed
        )
    }

    fun RaiseIndentSpare.toIndentRequest(): IndentRequest {
        return IndentRequest(
            name = this.name,
            qty = this.qty.toString(), // Convert quantity to a string, as IndentRequest uses a String for qty
            id = this.id
        )
    }

    private fun createTicketDetailsSummary(): TicketDetailsSummary {
        return TicketDetailsSummary(
            ticketAuditId = ticketAuditId!!,
            ticketId = ticketId!!,
            tasks = summaryTasksList,
            attachments = listOf(), // Assuming you have a way to collect attachments
            usedSpareIds = spareList.map { it.id },
            indentRequest = RaisedIndentandSpareList.map { it.toIndentRequest() }
        )
    }

    private fun createJsonRequestBody(): RequestBody {
        // Create JSON Object
        val jsonObject = JSONObject()

        // Add ticket details
        jsonObject.put("ticketAuditId", ticketAuditId ?: 0)
        jsonObject.put("ticketId", ticketId ?: 0)

        // Create and add tasks array
        val tasksArray = JSONArray()
        ticketdetailsSummary.tasks.forEach { task ->
            val taskJsonObject = JSONObject()
            taskJsonObject.put("taskId", task.taskId)
            taskJsonObject.put("taskTitle", task.taskTitle)
            taskJsonObject.put("status", task.status)
            taskJsonObject.put("openReason", task.openReason)
            taskJsonObject.put("dependency", task.dependency)
            taskJsonObject.put("fixedType", task.fixedType ?: JSONObject.NULL)
            taskJsonObject.put("fixedReason", task.fixedReason ?: JSONObject.NULL)
            taskJsonObject.put("remarks", task.remarks)
            taskJsonObject.put("sparesUsed", task.sparesUsed)
            tasksArray.put(taskJsonObject)
        }
        jsonObject.put("tasks", tasksArray)

        // Add attachments (assuming a list of URLs)
        val attachmentsArray = JSONArray()
        // Assuming you have a list of attachment URLs or IDs
        ticketdetailsSummary.attachments.forEach { attachment ->
            attachmentsArray.put(attachment)
        }
        jsonObject.put("attachments", attachmentsArray)

        // Add used spare IDs
        val usedSpareIdsArray = JSONArray()
        ticketdetailsSummary.usedSpareIds.forEach { usedSpareIdsArray.put(it) }
        jsonObject.put("usedSpareIds", usedSpareIdsArray)

        // Add indent requests
        val indentRequestsArray = JSONArray()
        ticketdetailsSummary.indentRequest.forEach { indentRequest ->
            val indentRequestJsonObject = JSONObject()
            indentRequestJsonObject.put("name", indentRequest.name)
            indentRequestJsonObject.put("qty", indentRequest.qty)
            indentRequestJsonObject.put("id", indentRequest.id)
            indentRequestsArray.put(indentRequestJsonObject)
        }
        jsonObject.put("indentRequest", indentRequestsArray)

        // Convert JSON object to string and create RequestBody
        val jsonString = jsonObject.toString()
        println("Json: $jsonString")
        return jsonString.toRequestBody("application/json".toMediaTypeOrNull())
    }

    private fun setupClickListeners() {


        ticket_rel_layout.setOnClickListener {
            // Toggle the flag state
            isGeneralInfoOpen = !isGeneralInfoOpen
            if (isGeneralInfoOpen) {
                // Code to open general_info_title
                ticket_layout.visibility = View.VISIBLE
            } else {
                // Code to close general_info_title
                ticket_layout.visibility = View.GONE
            }
        }

        task_rel_layout.setOnClickListener {
            // Toggle the flag state
            istaskInfoOpen = !istaskInfoOpen
            if (istaskInfoOpen) {
                // Code to open general_info_title
                ticketdetails_taskinfo_rcv.visibility = View.VISIBLE
            } else {
                // Code to close general_info_title
                ticketdetails_taskinfo_rcv.visibility = View.GONE
            }
        }

        requested_indentandspare_rel_layout.setOnClickListener {
            // Toggle the flag state
            reqIndentOpen = !reqIndentOpen
            if (reqIndentOpen) {
                // Code to open general_info_title
                requested_indentandspare_layout.visibility = View.VISIBLE
                raised_indentandspare_details_rcv.visibility = View.VISIBLE
            } else {
                // Code to close general_info_title
                requested_indentandspare_layout.visibility = View.GONE
                raised_indentandspare_details_rcv.visibility = View.GONE
            }
        }

        indentandspare_rel_layout.setOnClickListener {
            // Toggle the flag state
            indentsparesInfoOpen = !indentsparesInfoOpen
            if (indentsparesInfoOpen) {
                // Code to open general_info_title
                indentandspare_layout.visibility = View.VISIBLE
            } else {
                // Code to close general_info_title
                indentandspare_layout.visibility = View.GONE
            }
        }

        img_helpdesk_back.setOnClickListener {
            navigateBack()
        }

        logout_img.setOnClickListener {
            showLogoutDialog()
        }

        btn_cancel.setOnClickListener {
            navigateBack()
        }
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext(),R.style.CustomAlertDialogTheme)
        builder.setTitle("Logout Confirmation")
        builder.setMessage("Are you sure you want to log out?")
        builder.setPositiveButton("Yes") { _, _ ->
            myPreferences.clearPreferences()
            findNavController().navigate(R.id.action_summary_fragment_to_login)
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateBack()
                    // Do nothing
                }
            })
    }

    private fun initializeTicketDetailsSummary() {
        ticketdetailsSummary = createTicketDetailsSummary()
    }
}
