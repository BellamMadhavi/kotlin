package com.ivis.qcauditapp.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ivis.qcauditapp.utils.QcPreferences
import com.ivis.qcauditapp.viewmodels.maintenance.tickets.SchedulesViewModel
import com.qcauditapp.ApplicationController
import com.qcauditapp.R
import com.qcauditapp.viewmodels.MainViewModelFactory
import javax.inject.Inject
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.adapter.maintenance.tickets.TicketsAdapter
import com.ivis.qcauditapp.models.IndentItem
import com.ivis.qcauditapp.models.SpareBagItem
import com.ivis.qcauditapp.models.Ticket
import com.ivis.qcauditapp.models.TicketTask
import com.ivis.qcauditapp.retrofit.ItemOnClickListener
import com.ivis.qcauditapp.viewmodels.maintenance.TicketTaskDbDataViewModel

class MaintenanceTicketsFragment  : Fragment(), ItemOnClickListener{

    lateinit var schedulesViewModel: SchedulesViewModel
    lateinit var ticketTaskDbDataViewModel: TicketTaskDbDataViewModel
    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    lateinit var ticketsAdapter: TicketsAdapter
    lateinit var Ticketsrecycler_view: RecyclerView
    lateinit var txt_username: TextView
    lateinit var img_inspection_back: ImageView
    lateinit var logout_img: ImageView
    lateinit var middle_layout: LinearLayout
    lateinit var noSchedulesMessage_ll: LinearLayout
    lateinit var layout_main_content: LinearLayout
    private var mDialog: Dialog? = null

    lateinit var myPreferences: QcPreferences

    val ticketsList = mutableListOf<Ticket>()
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_maintenance_tickets, container, false)

        myPreferences = QcPreferences(requireContext())

        mDialog =
            (requireActivity().application as ApplicationController).showLoadingDialog(requireContext(), requireActivity().resources.getString(R.string.loading))
        mDialog!!.show()

        var FirstName = myPreferences.getString("firstName","")
        var lastName = myPreferences.getString("lastName","")

        Ticketsrecycler_view = view.findViewById(R.id.Ticketsrecycler_view)
        img_inspection_back = view.findViewById(R.id.img_inspection_back)
        middle_layout = view.findViewById(R.id.middle_layout)
        middle_layout.visibility = View.GONE
        txt_username = view.findViewById(R.id.txt_username)
        logout_img = view.findViewById(R.id.logout_img)
        noSchedulesMessage_ll = view.findViewById(R.id.noSchedulesMessage_ll)
        layout_main_content = view.findViewById(R.id.layout_main_content)

        txt_username.text = FirstName +" "+ lastName

        (requireActivity().application as ApplicationController).applicationComponent.inject(this)
        schedulesViewModel = ViewModelProvider(this, mainViewModelFactory).get(SchedulesViewModel::class.java)
        ticketTaskDbDataViewModel = ViewModelProvider(this, mainViewModelFactory).get(TicketTaskDbDataViewModel::class.java)

        var access_token = myPreferences.getString("accessToken", "")
        var Customer_Id = myPreferences.getString("Customer-Id", "")
        var Tenant_Id = myPreferences.getInt("Tenant-Id", 0)
        var userId = myPreferences.getInt("userId", 0)

        val currentDate = myPreferences.getCurrentDate()

        schedulesViewModel.fetchSchedules(userId, currentDate,"Bearer "+access_token,"null",Tenant_Id)

        schedulesViewModel.TicketsData.removeObservers(requireActivity())

        schedulesViewModel.TicketsData.observe(requireActivity(), Observer { response ->
            if (response != null && response.errorCode.toInt() == 200) {

                if (mDialog!!.isShowing && mDialog != null) {
                    mDialog!!.dismiss()
                    mDialog!!.hide()
                }

                response.results.let { results ->
                    ticketsList.clear()

                    ticketsList.addAll(results.sortedBy { it.priority })

                    ticketsAdapter.notifyDataSetChanged()

                    if (ticketsList.isEmpty()) {
                        noSchedulesMessage_ll.visibility = View.VISIBLE
                        layout_main_content.visibility = View.GONE
                    } else {
                        noSchedulesMessage_ll.visibility = View.GONE
                        layout_main_content.visibility = View.VISIBLE
                    }
                }
            } else {

                if (mDialog!!.isShowing && mDialog != null) {
                    mDialog!!.dismiss()
                    mDialog!!.hide()
                }
            }
        })


        ticketsAdapter = TicketsAdapter(requireContext(), ticketsList,this)
        Ticketsrecycler_view.layoutManager = LinearLayoutManager(requireContext())
        Ticketsrecycler_view.adapter = ticketsAdapter

        img_inspection_back.setOnClickListener{
            findNavController().navigate(R.id.action_maintenance_tickets_fragment_to_dashboard_fragment)
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
                    findNavController().navigate(R.id.action_maintenance_tickets_fragment_to_login_fragment)
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

    override fun MaintanenceTaskClickListener(position: Int) {
        TODO("Not yet implemented")
    }

    override fun MaintanenceTicketsClickListener(position: Int) {
            val clickedTicket = ticketsList[position]
            // Save the selected ticket details in preferences
            myPreferences.saveInt("TicketId",clickedTicket.ticketId)
            clickedTicket.task?.let { myPreferences.saveString("TicketName", it) }
            val status = clickedTicket.ticketStatus ?: "NA"
            myPreferences.saveString("TicketStatus", status)
            myPreferences.saveString("selected_taskname", clickedTicket.task.toString())
            myPreferences.saveString("sitename", clickedTicket.siteName.toString())
            myPreferences.saveString("attended", clickedTicket.attended.toString())

        myPreferences.saveInt("TicketAuditId", clickedTicket.ticketAuditId)
            findNavController().navigate(
                R.id.action_maintenance_tickets_fragment_to_maintenance_ticket_details_fragment)
        }

    override fun onIndentitemClick(indentItem: IndentItem, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onSpareitemClick(spareitem: SpareBagItem, isSelected: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onRadioButtonClicked(position: Int, selectedOption: String, taskitem: TicketTask) {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing
                findNavController().navigate(R.id.action_maintenance_tickets_fragment_to_dashboard_fragment)

            }
        })
    }

}
