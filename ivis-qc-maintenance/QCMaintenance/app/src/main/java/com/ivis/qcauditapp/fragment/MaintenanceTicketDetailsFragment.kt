package com.ivis.qcauditapp.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.ivis.qcauditapp.adapter.maintenance.TicketDetailsSchedulesInfoAdapter
import com.ivis.qcauditapp.adapter.maintenance.TicketTaskdetailsSpareIndentInfoAdapter
import com.ivis.qcauditapp.adapter.maintenance.TicketdetailsTaskAdapter
import com.ivis.qcauditapp.models.SchInfoHist
import com.ivis.qcauditapp.models.SpareIndent
import com.ivis.qcauditapp.models.TicketTask
import com.ivis.qcauditapp.utils.QcPreferences
import com.ivis.qcauditapp.viewmodels.maintenance.TicketTaskDbDataViewModel
import com.ivis.qcauditapp.viewmodels.maintenance.tickets.TicketDetailsViewModel
import com.qcauditapp.ApplicationController
import com.qcauditapp.R
import com.qcauditapp.viewmodels.MainViewModelFactory
import javax.inject.Inject

class MaintenanceTicketDetailsFragment : Fragment()  {

    lateinit var ticketDetailsViewModel: TicketDetailsViewModel
    lateinit var ticketTaskDbDataViewModel: TicketTaskDbDataViewModel
    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    lateinit var txt_ticket_name: TextView
    lateinit var txt_ticket_id: TextView
    lateinit var img_ticketdetails_back: ImageView
    lateinit var startbutton: MaterialButton
    private var ticketAuditId: Int? = null
    private var ticketId: Int? = null
    private var ticketStatus: String? = null

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

    lateinit var ticketdetails_taskinfo_rcv: RecyclerView
    lateinit var ticketdetails_scheduleinfo_rcv: RecyclerView
    lateinit var ticketdetails_IndentsSpares_rcv: RecyclerView
    private lateinit var title : String
    private lateinit var attended_status : String
    lateinit var logout_img: ImageView

    lateinit var general_info_title: RelativeLayout
    lateinit var general_info_layout: LinearLayout
    lateinit var people_title: RelativeLayout
    lateinit var people_info_layout: LinearLayout
    lateinit var task_title: RelativeLayout


    lateinit var schedules_title: RelativeLayout
    lateinit var indentspares_title: RelativeLayout
    private var isGeneralInfoOpen = false
    private var ispeopleInfoOpen = false
    private var istaskInfoOpen = false
    private var isschedulesInfoOpen = false
    private var isindentsparesInfoOpen = false

    private var mDialog: Dialog? = null

    lateinit var myPreferences: QcPreferences
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_maintenance_ticket_details, container, false)

        myPreferences = QcPreferences(requireContext())

        mDialog =
            (requireActivity().application as ApplicationController).showLoadingDialog(requireContext(), requireActivity().resources.getString(R.string.loading))
        mDialog!!.show()

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

        general_info_layout = view.findViewById(R.id.general_info_layout)
        general_info_title = view.findViewById(R.id.general_info_title)
        people_title = view.findViewById(R.id.people_title)
        people_info_layout = view.findViewById(R.id.people_info_layout)
        task_title = view.findViewById(R.id.task_title)
        schedules_title = view.findViewById(R.id.schedules_title)
        indentspares_title = view.findViewById(R.id.indentspares_title)

        ticketdetails_taskinfo_rcv  = view.findViewById(R.id.ticketdetails_taskinfo_rcv)
        img_ticketdetails_back  = view.findViewById(R.id.img_ticketdetails_back)
        logout_img  = view.findViewById(R.id.logout_img)
        ticketdetails_scheduleinfo_rcv  = view.findViewById(R.id.ticketdetails_scheduleinfo_rcv)
        ticketdetails_IndentsSpares_rcv  = view.findViewById(R.id.ticketdetails_IndentsSpares_rcv)
        startbutton  = view.findViewById(R.id.btn_start_task)

        txt_ticket_name  = view.findViewById(R.id.txt_ticket_name)
        txt_ticket_id  = view.findViewById(R.id.txt_ticket_id)

        val txtPeopledateCreateby: TextView = view.findViewById(R.id.txt_peopledate_createby)
        val txtPeopledateLastvisitedby: TextView = view.findViewById(R.id.txt_peopledate_lastvisitedby)
        val txtPeopledateFixedby: TextView = view.findViewById(R.id.txt_peopledate_fixedby)
        val txtPeopledateClosedby: TextView = view.findViewById(R.id.txt_peopledate_closedby)
        val txtPeopledateLastScheduledto: TextView = view.findViewById(R.id.txt_peopledate_lastScheduledto)
        val txtPeopledateCreatedtime: TextView = view.findViewById(R.id.txt_peopledate_createdtime)
        val txtPeopledateLastVisiteddate: TextView = view.findViewById(R.id.txt_peopledate_lastVisiteddate)
        val txtPeopledateFixeddate: TextView = view.findViewById(R.id.txt_peopledate_fixeddate)
        val txtPeopledateCloseddate: TextView = view.findViewById(R.id.txt_peopledate_closeddate)
        val txtPeopledateLastsched: TextView = view.findViewById(R.id.txt_peopledate_lastsched)


        (requireActivity().application as ApplicationController).applicationComponent.inject(this)
        ticketDetailsViewModel = ViewModelProvider(this, mainViewModelFactory).get(TicketDetailsViewModel::class.java)
        ticketTaskDbDataViewModel = ViewModelProvider(this, mainViewModelFactory).get(TicketTaskDbDataViewModel::class.java)

        var access_token = myPreferences.getString("accessToken", "")
        var Customer_Id = myPreferences.getString("Customer-Id", "")
        var Tenant_Id = myPreferences.getInt("Tenant-Id", 0)
        var userId = myPreferences.getInt("userId", 0)
        ticketId = myPreferences.getInt("TicketId", 0)
        txt_ticket_id.text = ticketId.toString()
        title = myPreferences.getString("selected_taskname", "")
        attended_status = myPreferences.getString("attended", "")

        ticketStatus = myPreferences.getString("TicketStatus", "NA")

        ticketAuditId = myPreferences.getInt("TicketAuditId", 0)

        // Your click listener for general_info_title

        general_info_title.setOnClickListener {

            // Toggle the flag state
            isGeneralInfoOpen = !isGeneralInfoOpen

            if (isGeneralInfoOpen) {
                // Code to open general_info_title
                general_info_layout.visibility = View.VISIBLE
            } else {
                // Code to close general_info_title
                general_info_layout.visibility = View.GONE

            }
        }

        people_info_layout.visibility = View.GONE

        ticketdetails_taskinfo_rcv.visibility = View.GONE

        ticketdetails_scheduleinfo_rcv.visibility = View.GONE

        ticketdetails_IndentsSpares_rcv.visibility = View.GONE

        people_title.setOnClickListener {

            // Toggle the flag state
            ispeopleInfoOpen = !ispeopleInfoOpen
            if (ispeopleInfoOpen) {
                // Code to open general_info_title
                people_info_layout.visibility = View.VISIBLE
            } else {
                // Code to close general_info_title
                people_info_layout.visibility = View.GONE
            }

        }

        task_title.setOnClickListener {
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

        schedules_title.setOnClickListener {
            // Toggle the flag state
            isschedulesInfoOpen = !isschedulesInfoOpen
            if (isschedulesInfoOpen) {
                // Code to open general_info_title
                ticketdetails_scheduleinfo_rcv.visibility = View.VISIBLE
            } else {
                // Code to close general_info_title
                ticketdetails_scheduleinfo_rcv.visibility = View.GONE
            }

        }

        indentspares_title.setOnClickListener {
            // Toggle the flag state
            isindentsparesInfoOpen = !isindentsparesInfoOpen
            if (isindentsparesInfoOpen) {
                // Code to open general_info_title
                ticketdetails_IndentsSpares_rcv.visibility = View.VISIBLE
            } else {
                // Code to close general_info_title
                ticketdetails_IndentsSpares_rcv.visibility = View.GONE
            }

        }

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
                txt_ticketinfo_ticketType .text =  responseList.results.ticketInfo.ticketType
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


                // Set text for all TextViews from responseList
                txtPeopledateCreateby.text = responseList.results.people.createdby
                txtPeopledateLastvisitedby.text = responseList.results.people.visitedBy
                txtPeopledateFixedby.text = responseList.results.people.fixedBy
                txtPeopledateClosedby.text = responseList.results.people.closedBy
                txtPeopledateLastScheduledto.text = responseList.results.people.scheduledTo
                txtPeopledateCreatedtime.text = responseList.results.people.createdTime
                txtPeopledateLastVisiteddate.text = responseList.results.people.lastVisitedDate
                txtPeopledateFixeddate.text = responseList.results.people.fixedDate
                txtPeopledateCloseddate.text = responseList.results.people.closedDate
                txtPeopledateLastsched.text = responseList.results.people.lastScheduledDate

                txt_ticket_name.text = responseList.results.ticketInfo.title

                val ticketTaskList: List<TicketTask> = responseList.results.ticketTask

                val ticketTaskscheduleinfo: List<SchInfoHist> = responseList.results.schInfoHist

                val ticketTaskSpareindentinfo: List<SpareIndent> = responseList.results.spareIndent

                val adapter = TicketdetailsTaskAdapter(ticketTaskList)
                ticketdetails_taskinfo_rcv.layoutManager = LinearLayoutManager(requireContext())
                ticketdetails_taskinfo_rcv.adapter = adapter

                val scheduleadapter = TicketDetailsSchedulesInfoAdapter(ticketTaskscheduleinfo)
                ticketdetails_scheduleinfo_rcv.layoutManager = LinearLayoutManager(requireContext())
                ticketdetails_scheduleinfo_rcv.adapter = scheduleadapter

                val spareindentinfoadapter = TicketTaskdetailsSpareIndentInfoAdapter(ticketTaskSpareindentinfo)
                ticketdetails_IndentsSpares_rcv.layoutManager = LinearLayoutManager(requireContext())
                ticketdetails_IndentsSpares_rcv.adapter = spareindentinfoadapter


                if (responseList.results.ticketInfo.status.equals("Done", ignoreCase = true)) {
                    txt_ticketType_status.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorCompletedInspection))
                } else {
                    txt_ticketType_status.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                }


                if(ticketStatus.equals("New", true) || ticketStatus.equals("Open", true)) {
                    if (attended_status.equals("Pending",true)){
                        startbutton.visibility = View.VISIBLE
                    } else{
                        startbutton.visibility = View.GONE
                    }
                } else {
                    startbutton.visibility = View.GONE
                }
            } else {
                if (mDialog!!.isShowing && mDialog != null) {
                    mDialog!!.dismiss()
                    mDialog!!.hide()
                }
            }
        })


        startbutton.setOnClickListener {
            findNavController().navigate(R.id.action_maintenance_ticket_details_fragment_maintenance_task_list_fragment)
        }

        img_ticketdetails_back.setOnClickListener{
            findNavController().navigate(R.id.action_maintenance_ticket_details_fragment_to_maintenance_tickets_fragment)
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
                    findNavController().navigate(R.id.action_maintenance_ticket_details_fragment_to_login_fragment)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing
                findNavController().navigate(R.id.action_maintenance_ticket_details_fragment_to_maintenance_tickets_fragment)

            }
        })
    }
}