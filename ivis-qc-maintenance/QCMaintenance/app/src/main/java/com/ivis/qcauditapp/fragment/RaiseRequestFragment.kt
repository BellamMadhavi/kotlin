package com.ivis.qcauditapp.fragment

import android.text.Editable
import android.text.TextWatcher
import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.adapter.maintenance.RaiseRquestAdapter
import com.ivis.qcauditapp.db.RaiseIndentSpare
import com.ivis.qcauditapp.models.RequestSpareItem
import com.ivis.qcauditapp.models.Result
import com.ivis.qcauditapp.utils.QcPreferences
import com.ivis.qcauditapp.viewmodels.maintenance.RaiseRequestViewModel
import com.ivis.qcauditapp.viewmodels.maintenance.TicketTaskDbDataViewModel
import com.qcauditapp.ApplicationController
import com.qcauditapp.R
import com.qcauditapp.viewmodels.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RaiseRequestFragment  : Fragment() {

    lateinit var raiseRequestViewModel: RaiseRequestViewModel
    lateinit var raiseRquestAdapter: RaiseRquestAdapter
    lateinit var ticketTaskDbDataViewModel: TicketTaskDbDataViewModel

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var indent_request_autocomplete : AutoCompleteTextView
    lateinit var myPreferences: QcPreferences
    private lateinit var state : String
    private lateinit var title : String
    private var ticketId : Int? = null
    private var taskId : Int? = null
    private var ticketAuditId: Int? = null
    lateinit var site_name: TextView
    lateinit var indentReqAddButton: Button
    lateinit var continue_btn: Button
    lateinit var btn_cancel: Button
    lateinit var Indent_recyclerView: RecyclerView
    val selectedItems: MutableList<RequestSpareItem> = mutableListOf()
    val nameToRequestSpareItemMap = mutableMapOf<String, RequestSpareItem>()
    lateinit var clearIndentRequest: ImageView
    var isFocused = false

    private lateinit var txt_ticket_id: TextView
    private lateinit var txt_ticket_name: TextView
    private lateinit var img_raiseIndentSpare_back: ImageView

    private var mDialog: Dialog? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_indent_request, container, false)

        myPreferences = QcPreferences(requireContext())

        mDialog =
            (requireActivity().application as ApplicationController).showLoadingDialog(requireContext(), requireActivity().resources.getString(R.string.loading))
        mDialog!!.show()

        indent_request_autocomplete = view.findViewById(R.id.indent_request_autocomplete)
        site_name = view.findViewById(R.id.site_name)
        indentReqAddButton = view.findViewById(R.id.indentReqAddButton)
        Indent_recyclerView = view.findViewById(R.id.Indent_recyclerView)
        continue_btn = view.findViewById(R.id.continue_btn)
        btn_cancel = view.findViewById(R.id.btn_cancel)
        clearIndentRequest = view.findViewById(R.id.clearIndentRequest)

        img_raiseIndentSpare_back = view.findViewById(R.id.img_raiseIndentSpare_back)

        txt_ticket_id = view.findViewById(R.id.txt_ticket_id)
        txt_ticket_name = view.findViewById(R.id.txt_ticket_name)


        (requireActivity().application as ApplicationController).applicationComponent.inject(this)
        raiseRequestViewModel = ViewModelProvider(this, mainViewModelFactory).get(RaiseRequestViewModel::class.java)
        ticketTaskDbDataViewModel = ViewModelProvider(this, mainViewModelFactory).get(TicketTaskDbDataViewModel::class.java)

        var access_token = myPreferences.getString("accessToken", "")
        var Customer_Id = myPreferences.getString("Customer-Id", "")
        var Tenant_Id = myPreferences.getInt("Tenant-Id", 0)
        var userId = myPreferences.getInt("userId", 0)
        ticketId = myPreferences.getInt("TicketId", 0)
        taskId = myPreferences.getInt("taskId", 0)
        ticketAuditId = myPreferences.getInt("TicketAuditId", 0)
        state = myPreferences.getString("Status", "")
        title = myPreferences.getString("selected_taskname", "")
        site_name.text ="You are in"+" "+myPreferences.getString("sitename", "")

        txt_ticket_id.text = ticketId.toString()
        txt_ticket_name.text = title

        raiseRequestViewModel.fetchraiserequest("Bearer $access_token", "null", Tenant_Id)
        raiseRequestViewModel.RaiseRequestlist.removeObservers(requireActivity())
        raiseRequestViewModel.RaiseRequestlist.observe(viewLifecycleOwner, Observer { requestSpareItem ->
            if (requestSpareItem != null && requestSpareItem.errorCode == "200") {

                if (mDialog!!.isShowing && mDialog != null) {
                    mDialog!!.dismiss()
                    mDialog!!.hide()
                }

                val resultItems = requestSpareItem.results
                val resultNames = resultItems.map { it.name }
                val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, resultNames)
                indent_request_autocomplete.setAdapter(arrayAdapter)

                // Populate the name to Result map
                resultItems.forEach { result ->
                    val requestSpareItem = RequestSpareItem(
                        results = listOf(result),
                        errorMessage = null,
                        errorCode = "200"
                    )
                    nameToRequestSpareItemMap[result.name] = requestSpareItem
                }
            } else {

                if (mDialog!!.isShowing && mDialog != null) {
                    mDialog!!.dismiss()
                    mDialog!!.hide()
                }
            }
        })

        indent_request_autocomplete.setOnClickListener {
            indent_request_autocomplete.showDropDown()
        }

        img_raiseIndentSpare_back.setOnClickListener {
            findNavController().navigate(R.id.action_indent_request_fragment_to_task_form_fragment)
        }


        clearIndentRequest.setOnClickListener {
            indent_request_autocomplete.setText("")
        }

        indent_request_autocomplete.setOnFocusChangeListener { _, hasFocus ->
            isFocused = hasFocus
            // Show or hide the clear image based on focus and text
            updateClearImageVisibility()
        }

        indent_request_autocomplete.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Show or hide the clear image based on focus and text
                updateClearImageVisibility()
            }

            override fun afterTextChanged(s: Editable?) {
                // No implementation needed
            }
        })

        indent_request_autocomplete.setOnItemClickListener { parent, view, position, id ->
            val selectedData = parent.getItemAtPosition(position).toString()
            indent_request_autocomplete.setText(selectedData) // Set the selected item in the AutoCompleteTextView
            updateClearImageVisibility()
        }


        indentReqAddButton.setOnClickListener {
            val selectedName = indent_request_autocomplete.text.toString().trim()
            if (selectedName.isNotEmpty() && nameToRequestSpareItemMap.containsKey(selectedName)) {
                val selectedItem = nameToRequestSpareItemMap[selectedName]

                selectedItem?.let { item ->
                    // Check if the item is already in the list
                    val existingItem = selectedItems.find { it.results[0].id == item.results[0].id }

                    if (existingItem != null) {
                        // If item exists, increment its quantity
                        existingItem.results[0].qty += 1
                    } else {
                        // If item does not exist, add it to the list
                        selectedItems.add(item)
                    }
                    // Notify the adapter of the change
                    raiseRquestAdapter.notifyDataSetChanged()
                }
            } else {
                Toast.makeText(requireContext(), "Please select a valid item", Toast.LENGTH_SHORT).show()
            }
        }


        raiseRquestAdapter = RaiseRquestAdapter( context = requireContext(), selectedItems,
            onQuantityUpdated = { updatedItem ->
                CoroutineScope(Dispatchers.IO).launch {
                    val existingEntry = ticketTaskDbDataViewModel.getRaiseIndentSpareById(updatedItem.results[0].id, ticketId!!, taskId!!)
                    if (existingEntry != null) {
                        // Update the existing entry in the database
                        ticketTaskDbDataViewModel.updateRaiseIndentSpareQuantity(updatedItem.results[0].id, ticketId!!, taskId!!, updatedItem.results[0].qty)
                    }
                }
            },
            onDeleteItem = { deletedItem ->
                // Get the item name and quantity
                val itemName = deletedItem.results[0].name
                val itemQuantity = deletedItem.results[0].qty

                // Show confirmation dialog with item name and quantity
                AlertDialog.Builder(requireContext(),R.style.CustomAlertDialogTheme)
                    .setTitle("Delete Confirmation")
                    .setMessage("Are you sure you want to delete the item '$itemName' with quantity $itemQuantity?")
                    .setPositiveButton("Yes") { dialog, which ->
                        // User confirmed deletion
                        CoroutineScope(Dispatchers.IO).launch {
                            // Delete the item from the database
                            ticketTaskDbDataViewModel.deleteRaiseIndentSpare(
                                deletedItem.results[0].id, ticketId!!, taskId!!
                            )
                            // Remove the item from the list in the UI
                            withContext(Dispatchers.Main) {
                                selectedItems.remove(deletedItem)
                                raiseRquestAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                    .setNegativeButton("No") { dialog, which ->
                        // User canceled deletion
                        dialog.dismiss()
                    }
                    .show()
            }
        )
        Indent_recyclerView.adapter = raiseRquestAdapter
        Indent_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        // Fetch data from the database and observe it
        ticketTaskDbDataViewModel.getraisedindentsandsparesbyticket(ticketId!!, taskId!!)
        ticketTaskDbDataViewModel.ticketbyRequestedIndentandSpare.observe(viewLifecycleOwner, Observer { dbItems ->
            dbItems?.let {
                // Clear the current list
                selectedItems.clear()
                dbItems.forEach { item ->
                    // Map RaiseIndentSpare to Result
                    val result = Result(
                        id = item.id,
                        name = item.name,
                        qty = item.qty
                    )
                    // Create a RequestSpareItem using the mapped Result
                    val requestSpareItem = RequestSpareItem(
                        results = listOf(result),
                        errorMessage = null,
                        errorCode = "200"
                    )
                    selectedItems.add(requestSpareItem)
                    nameToRequestSpareItemMap[item.name] = requestSpareItem
                }


                // Update the adapter with database data
                raiseRquestAdapter.notifyDataSetChanged()

                val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, selectedItems)
                indent_request_autocomplete.setAdapter(arrayAdapter)
            }

            if (mDialog!!.isShowing && mDialog != null) {
                mDialog!!.dismiss()
                mDialog!!.hide()
            }
        })
        continue_btn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val raiseIndentSpareList = selectedItems.map { item ->
                    RaiseIndentSpare(
                        ticketId = ticketId ?: 0,
                        taskId = taskId ?: 0,
                        id = item.results.get(0).id,
                        name =item.results.get(0).name,
                        qty = item.results.get(0).qty
                    )
                }
                ticketTaskDbDataViewModel.insertRaiseIndentSpare(raiseIndentSpareList)
            }
            // Navigate to the next fragment
            findNavController().navigate(R.id.action_indent_request_fragment_to_summary_fragment)
        }

        btn_cancel.setOnClickListener {
            findNavController().navigate(R.id.action_indent_request_fragment_to_task_form_fragment)
        }

        return view
    }

    fun updateClearImageVisibility() {
        if (isFocused && !indent_request_autocomplete.text.isNullOrEmpty()) {
            clearIndentRequest.visibility = View.VISIBLE
        } else {
            clearIndentRequest.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing
                findNavController().navigate(R.id.action_indent_request_fragment_to_task_form_fragment)

            }
        })

    }

}
