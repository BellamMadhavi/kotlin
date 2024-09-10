package com.ivis.qcauditapp.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.adapter.maintenance.indent.SpareAdapter
import com.ivis.qcauditapp.utils.QcPreferences
import com.ivis.qcauditapp.viewmodels.maintenance.indent.SpareViewModel
import com.qcauditapp.ApplicationController
import com.qcauditapp.R
import com.qcauditapp.viewmodels.MainViewModelFactory
import javax.inject.Inject
import androidx.lifecycle.Observer
import com.ivis.qcauditapp.models.SpareBagItem
import com.ivis.qcauditapp.retrofit.ItemOnClickListener
import com.ivis.qcauditapp.db.SpareBagEntity
import com.ivis.qcauditapp.models.IndentItem
import com.ivis.qcauditapp.models.TicketTask
import com.ivis.qcauditapp.viewmodels.maintenance.TicketTaskDbDataViewModel

class SparsDetailsFragment : Fragment(), ItemOnClickListener {

    lateinit var spareViewModel: SpareViewModel
    lateinit var ticketTaskDbDataViewModel: TicketTaskDbDataViewModel

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    private lateinit var spareAdapter: SpareAdapter
    private lateinit var spare_recyclerView: RecyclerView
    private lateinit var continue_btn: Button
    private lateinit var btn_cancel: Button
    private lateinit var logout_img: ImageView
    private lateinit var img_helpdesk_back: ImageView

    private lateinit var txt_ticket_id: TextView
    private lateinit var txt_ticket_name: TextView
    private lateinit var no_records_found_txt: TextView
    private lateinit var spares_view_layout: LinearLayout

    private lateinit var title : String
    lateinit var site_name: TextView

    private var ticketId : Int? = null
    private var ticketAuditId: Int? = null
    private var taskId: Int? = null

    private var mDialog: Dialog? = null

    private lateinit var progressBar: ProgressBar
    private val selectedSpares: MutableSet<Int> = mutableSetOf()
    private val spareList: MutableList<SpareBagItem> = mutableListOf()

    private lateinit var preferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_spars_details, container, false)

        val myPreferences = QcPreferences(requireContext())

        mDialog = (requireActivity().application as ApplicationController)
            .showLoadingDialog(
                requireContext(),
                requireActivity().resources.getString(R.string.loading)
            )
        //mDialog!!.show()

        spare_recyclerView = view.findViewById(R.id.spare_recyclerView)
        continue_btn = view.findViewById(R.id.continue_btn)
        logout_img = view.findViewById(R.id.logout_img)
        img_helpdesk_back = view.findViewById(R.id.img_helpdesk_back)
        site_name = view.findViewById(R.id.site_name)
        btn_cancel = view.findViewById(R.id.btn_cancel)
        progressBar = view.findViewById(R.id.progressBar)

        preferences = requireContext().getSharedPreferences("qc_audit_app", 0)

        var access_token = myPreferences.getString("accessToken", "")
        var Customer_Id = myPreferences.getString("Customer-Id", "")
        var Tenant_Id = myPreferences.getInt("Tenant-Id", 0)
        var userId = myPreferences.getInt("userId", 0)
        ticketId = myPreferences.getInt("TicketId", 0)
        ticketAuditId = myPreferences.getInt("TicketAuditId", 0)
        title = myPreferences.getString("selected_taskname", "")
        taskId = myPreferences.getInt("taskId", 0)
        site_name.text = "You are in" + " " + myPreferences.getString("sitename", "")
        no_records_found_txt = view.findViewById(R.id.no_records_found_txt)
        spares_view_layout = view.findViewById(R.id.spares_view_layout)

        txt_ticket_id = view.findViewById(R.id.txt_ticket_id)
        txt_ticket_name = view.findViewById(R.id.txt_ticket_name)

        txt_ticket_id.text = ticketId.toString()
        txt_ticket_name.text = title

        (requireActivity().application as ApplicationController).applicationComponent.inject(this)
        spareViewModel =
            ViewModelProvider(this, mainViewModelFactory).get(SpareViewModel::class.java)
        ticketTaskDbDataViewModel =
            ViewModelProvider(this, mainViewModelFactory).get(TicketTaskDbDataViewModel::class.java)

        ticketTaskDbDataViewModel.getSpareBagsByTicketIdTaskId(ticketId!!, taskId!!)
        ticketTaskDbDataViewModel.spareDatabyTicketId.removeObservers(requireActivity())
        ticketTaskDbDataViewModel.spareDatabyTicketId.observe(
            viewLifecycleOwner,
            Observer { spare ->
                spare?.let {
                    selectedSpares.clear()
                    selectedSpares.addAll(it.map { spare -> spare.id })
                }
            })

        spareViewModel.fetchspares(ticketId!!, "Bearer " + access_token, "null", Tenant_Id)

        spareViewModel.SparesDatalist.removeObservers(requireActivity())

        spareViewModel.SparesDatalist.observe(viewLifecycleOwner, Observer { response ->

            if (response.errorCode.toInt() == 200) {

                if (mDialog!!.isShowing && mDialog != null) {
                    mDialog!!.dismiss()
                    mDialog!!.hide()
                }

                val sparedetails = response.results.spareBag

                // Add new items to the list only if they are not already present
                val currentSpareIds = spareList.map { it.id }.toSet()
                val newSpareDetails = sparedetails.filter { it.id !in currentSpareIds }

                if (newSpareDetails.isNotEmpty()) {
                    spareList.addAll(newSpareDetails)
                    spareAdapter.updateSelectedSpares(selectedSpares)
                    spareAdapter.notifyDataSetChanged()
                }

                // get the list of sapres from server, and merge the selected spares..
                val selectedIds = preferences.getString("selected_spares", "") ?: ""
                selectedSpares.clear()
                selectedSpares.addAll(selectedIds.split(",").mapNotNull { it.toIntOrNull() })
                spareAdapter.notifyDataSetChanged()

            } else {

                if (mDialog!!.isShowing && mDialog != null) {
                    mDialog!!.dismiss()
                    mDialog!!.hide()
                }
            }

            if (spareList.isNotEmpty()) {
                spares_view_layout.visibility = View.VISIBLE
                no_records_found_txt.visibility = View.GONE
            } else {
                spares_view_layout.visibility = View.GONE
                no_records_found_txt.visibility = View.VISIBLE
            }
        })

        // Initialize and set the adapter
        spareAdapter = SpareAdapter(requireContext(), spareList, this)
        spare_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        spare_recyclerView.adapter = spareAdapter


    continue_btn.setOnClickListener {
            saveSelectedSpares()
            findNavController().navigate(R.id.action_spare_details_fragment_to_summary_details_fragment)
        }

        img_helpdesk_back.setOnClickListener {
            findNavController().navigate(R.id.action_spare_details_fragment_to_maintenance_taskform_fragment)
        }

        btn_cancel.setOnClickListener{
            findNavController().navigate(R.id.action_spare_details_fragment_to_maintenance_taskform_fragment)
        }

        logout_img.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext(),R.style.CustomAlertDialogTheme)
            builder.setTitle("Logout Confirmation")
            builder.setMessage("Are you sure you want to log out?")
            builder.setPositiveButton("Yes") { dialog, _ ->
                val myPreferences = QcPreferences(requireContext())
                myPreferences.clearPreferences()
                ticketTaskDbDataViewModel.clearAllTickets()
                findNavController().navigate(R.id.action_spare_details_fragment_to_login_fragment)
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.show()
        }

        return view
    }

    override fun MaintanenceTaskClickListener(position: Int) {
        // Not yet implemented
    }

    override fun MaintanenceTicketsClickListener(position: Int) {
        // Not yet implemented
    }

    override fun onIndentitemClick(indentItem: IndentItem, position: Int) {
        // Not yet implemented
    }

    override fun onSpareitemClick(spareitem: SpareBagItem, isSelected: Boolean) {
        if (isSelected) {
            val sparebagEntity = SpareBagEntity(
                id = spareitem.id,
                ticketId = ticketId!!,
                taskId = taskId!!,
                reqType = spareitem.reqType,
                itemDescription = spareitem.itemDescription,
                qty = spareitem.qty,
                status = spareitem.status,
                serialNo = spareitem.serialNo,
                docketNumber = spareitem.docketNumber ?: "",
                dcNo = spareitem.dcNo,
                dispatchedDate = spareitem.dispatchedDate,
                receivedDate = spareitem.receivedDate ?: ""
            )
            ticketTaskDbDataViewModel.insertSpareBags(listOf(sparebagEntity))
        } else {
            ticketTaskDbDataViewModel.deleteSpare(ticketId!!, taskId!!, spareitem.id)
        }
    }

    private fun saveSelectedSpares() {
        val editor = preferences.edit()
        val selectedIds = selectedSpares.joinToString(",")
        editor.putString("selected_spares", selectedIds)
        editor.apply()

        val spareEntities = selectedSpares.map { spareId ->
            val spareItem = spareList.find { it.id == spareId }
            spareItem?.let {
                SpareBagEntity(
                    id = it.id,
                    ticketId = ticketId ?: 0, // Ensure ticketId is not null
                    taskId = taskId ?: 0,      // Ensure taskId is not null
                    reqType = it.reqType,
                    itemDescription = it.itemDescription,
                    qty = it.qty,
                    status = it.status,
                    serialNo = it.serialNo,
                    docketNumber = it.docketNumber ?: "",
                    dcNo = it.dcNo,
                    dispatchedDate = it.dispatchedDate,
                    receivedDate = it.receivedDate ?: ""
                )
            }
        }.filterNotNull() // Remove nulls from the list if any spareId does not match

        if (spareEntities.isNotEmpty()) {
            ticketTaskDbDataViewModel.insertSpareBags(spareEntities)
        }

        // Refresh the RecyclerView with updated selections
        spareAdapter.notifyDataSetChanged()
    }


    override fun onRadioButtonClicked(position: Int, selectedOption: String, taskitem: TicketTask) {
        // Not yet implemented
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing
                findNavController().navigate(R.id.action_spare_details_fragment_to_maintenance_taskform_fragment)

            }
        })

    }
}
