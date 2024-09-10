package com.ivis.qcauditapp.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ivis.qcauditapp.adapter.MenuListAdapter
import com.ivis.qcauditapp.models.Dashboarditems
import com.ivis.qcauditapp.utils.QcPreferences
import com.ivis.qcauditapp.viewmodels.FRreportViewModel
import com.ivis.qcauditapp.viewmodels.maintenance.TicketTaskDbDataViewModel
import com.qcauditapp.ApplicationController
import com.qcauditapp.R
import com.qcauditapp.viewmodels.MainViewModelFactory
import javax.inject.Inject

class DashBoardFragment  : Fragment() {

    lateinit var fRreportViewModel: FRreportViewModel
    lateinit var ticketTaskDbDataViewModel: TicketTaskDbDataViewModel

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    lateinit var gridView: GridView
    lateinit var txt_username: TextView
    lateinit var middle_layout: LinearLayout
    lateinit var logout_img: ImageView
    lateinit var txt_requested_count: TextView
    lateinit var txt_approved_count: TextView
    lateinit var txt_delivered_count: TextView
    lateinit var txt_cancelled_count: TextView
    lateinit var itemsList: MutableList<Dashboarditems>

    private var mDialog: Dialog? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dash_board, container, false)

        gridView = view.findViewById(R.id.items_gridview)
        middle_layout = view.findViewById(R.id.middle_layout)
        logout_img = view.findViewById(R.id.logout_img)
        txt_requested_count = view.findViewById(R.id.txt_requested_count)
        txt_approved_count = view.findViewById(R.id.txt_approved_count)
        txt_delivered_count = view.findViewById(R.id.txt_delivered_count)
        txt_cancelled_count = view.findViewById(R.id.txt_cancelled_count)
        middle_layout.visibility = View.GONE
        itemsList = mutableListOf()

        val myPreferences = QcPreferences(requireContext())

        mDialog =
            (requireActivity().application as ApplicationController).showLoadingDialog(
                requireContext(),
                requireActivity().resources.getString(R.string.loading)
            )
        mDialog!!.show()

        var FirstName = myPreferences.getString("firstName", "")
        var lastName = myPreferences.getString("lastName", "")
        var userId = myPreferences.getInt("userId", 0)
        var access_token = myPreferences.getString("accessToken", "")
        var Customer_Id = myPreferences.getString("Customer-Id", "")
        var Tenant_Id = myPreferences.getInt("Tenant-Id", 0)

        (requireActivity().application as ApplicationController).applicationComponent.inject(this)
        fRreportViewModel =
            ViewModelProvider(this, mainViewModelFactory).get(FRreportViewModel::class.java)
        ticketTaskDbDataViewModel =
            ViewModelProvider(this, mainViewModelFactory).get(TicketTaskDbDataViewModel::class.java)

        txt_username = view.findViewById(R.id.txt_username)
        txt_username.text = FirstName + " " + lastName

        itemsList.add(Dashboarditems("Start Maintenance", R.drawable.maintenance_icon, false))
        itemsList.add(Dashboarditems("Installation QC", R.drawable.installation_qc, false))
        itemsList.add(Dashboarditems("QC", R.drawable.qc, false))
        itemsList.add(Dashboarditems("Field Audit", R.drawable.field_audit, false))

        val dashboardItemAdapter = MenuListAdapter(this, itemsList)
        gridView.adapter = dashboardItemAdapter

        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            val clickedItem = itemsList[position]
            clickedItem.checked = !clickedItem.checked

            if (clickedItem.checked) {
                itemsList.forEach { it.checked = false }
                clickedItem.checked = true
            }
            dashboardItemAdapter.notifyDataSetChanged()

            if (clickedItem.itemName == "Start Maintenance") {
                when (clickedItem.itemName) {
                    "Start Maintenance" -> {
                        findNavController().navigate(R.id.action_DashboardFragment_to_MaintenanceTicketsFragment)
                    }
                }
            } else {
                Toast.makeText(context, "This action is disabled", Toast.LENGTH_SHORT).show()
            }
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
                    findNavController().navigate(R.id.action_DashboardFragment_to_login)
                }
                builder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.setCancelable(false)
                dialog.show()
            }
        })

        val handler = Handler(Looper.getMainLooper())
        var dataReceived = false


        fRreportViewModel.fetchfrreportlist(userId, "Bearer $access_token", "null", Tenant_Id)

        fRreportViewModel.FrreportDatalist.removeObservers(requireActivity())

        fRreportViewModel.FrreportDatalist.observe(requireActivity(), Observer { response ->
            if (response != null && response.errorCode!!.toInt() == 200) {
                dataReceived = true

                if (mDialog!!.isShowing && mDialog != null) {
                    mDialog!!.dismiss()
                    mDialog!!.hide()
                }
                txt_requested_count.text = response.results.tickets.toString()
                txt_approved_count.text = response.results.dispatchIndents.toString()
                txt_delivered_count.text = response.results.used.toString()
                txt_cancelled_count.text = response.results.toBeReturned.toString()
            } else {
                dataReceived = true
                if (mDialog!!.isShowing && mDialog != null) {
                    mDialog!!.dismiss()
                    mDialog!!.hide()
                }

                // Handle the error case
                Log.e("LoginFragment", "Error: ${response?.errorCode}")
                Toast.makeText(
                    requireContext(),
                    "Login failed: ${response?.errorCode}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        // Set up a delay to dismiss the dialog after 10 seconds if no data is received
        handler.postDelayed({
            if (!dataReceived) {
                if (mDialog?.isShowing == true) {
                    mDialog?.dismiss()
                }
                Toast.makeText(
                    requireContext(),
                    "No data received within the timeout period.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }, 10000)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Do nothing
                    findNavController().navigate(R.id.action_DashboardFragment_to_login)
                }

        })
    }
}
