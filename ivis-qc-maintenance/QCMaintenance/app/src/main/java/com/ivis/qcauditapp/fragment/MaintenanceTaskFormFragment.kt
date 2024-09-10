package com.ivis.qcauditapp.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.ivis.qcauditapp.adapter.maintenance.UploadImagesAdapter
import com.ivis.qcauditapp.db.TaskEntity
import com.ivis.qcauditapp.db.UserDao
import com.ivis.qcauditapp.utils.QcPreferences
import com.ivis.qcauditapp.viewmodels.MaintenanceResponseViewModel
import com.ivis.qcauditapp.viewmodels.maintenance.TicketTaskDbDataViewModel
import com.qcauditapp.ApplicationController
import com.qcauditapp.R
import com.qcauditapp.viewmodels.MainViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class MaintenanceTaskFormFragment : Fragment() {

    lateinit var maintenanceResponseViewModel: MaintenanceResponseViewModel
    lateinit var ticketTaskDbDataViewModel: TicketTaskDbDataViewModel
    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var uploadImgLayout: LinearLayout
    private lateinit var uploadImagesAdapter: UploadImagesAdapter
    private lateinit var gridView_image: GridView
    private lateinit var btn_continue: Button
    private lateinit var btn_cancel: Button
    private lateinit var logout_img: ImageView
    private lateinit var img_helpdesk_back: ImageView
    private lateinit var auto_type: AutoCompleteTextView
    private lateinit var auto_reason: AutoCompleteTextView
    private lateinit var txt_ticket_id: TextView
    private lateinit var txt_ticket_name: TextView
    private lateinit var radio_yes: RadioButton
    private lateinit var radio_no: RadioButton
    private lateinit var radioGroup: RadioGroup
    private lateinit var comments_txt: EditText
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var photoUri: Uri? = null
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private var imageList = ArrayList<String>()
    private var currentTicketId: Int? = null

    private var ticketAuditId: Int? = null
    private lateinit var datalistType: List<String>
    private lateinit var datalistReason: List<String>

    private var dbData:Boolean = false
    private var selectedOption : String = "no"

    private lateinit var state : String
    private lateinit var statusfixed : String
    private lateinit var title : String
    private var ticketId : Int? = null
    private var taskId : Int? = null
    lateinit var site_name: TextView
    lateinit var cleartype: ImageView
    lateinit var clearReason: ImageView

    lateinit var autocomplete1_title: TextView
    lateinit var autocomplete2_title: TextView
    lateinit var comments_Textinputlayout: TextInputLayout

    lateinit var spare_used: LinearLayout
    var isAutoTypeFocused = false
    var isAutoReasonFocused = false
    lateinit var myPreferences: QcPreferences
    private var mDialog: Dialog? = null
    @Inject
    lateinit var userDao: UserDao

    private var selectedText = ""
    private lateinit var currentPhotoPath: String
    private lateinit var isSpareIndentUsed : String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.maintenance_taskform, container, false)

        myPreferences = QcPreferences(requireContext())

        /*  mDialog =
               (requireActivity().application as ApplicationController).showLoadingDialog(requireContext(), requireActivity().resources.getString(R.string.loading))
           mDialog!!.show()*/

        uploadImgLayout = view.findViewById(R.id.attach_layout)
        gridView_image = view.findViewById(R.id.gridView)
        btn_continue = view.findViewById(R.id.btn_continue)
        btn_cancel = view.findViewById(R.id.btn_cancel)
        auto_type = view.findViewById(R.id.auto_type)
        auto_reason = view.findViewById(R.id.auto_reason)
        img_helpdesk_back = view.findViewById(R.id.img_helpdesk_back)
        logout_img = view.findViewById(R.id.logout_img)
        txt_ticket_id = view.findViewById(R.id.txt_ticket_id)
        txt_ticket_name = view.findViewById(R.id.txt_ticket_name)
        radio_yes = view.findViewById(R.id.radio_yes)
        radio_no = view.findViewById(R.id.radio_no)
        radioGroup = view.findViewById(R.id.radioGroup)
        comments_txt = view.findViewById(R.id.comments_txt)
        site_name = view.findViewById(R.id.site_name)
        spare_used = view.findViewById(R.id.spare_used)
        cleartype = view.findViewById(R.id.cleartype)
        clearReason = view.findViewById(R.id.clearReason)
        autocomplete1_title = view.findViewById(R.id.autocomplete1_title)
        autocomplete2_title = view.findViewById(R.id.autocomplete2_title)
        comments_Textinputlayout = view.findViewById(R.id.comments_Textinputlayout)

        (requireActivity().application as ApplicationController).applicationComponent.inject(this)
        maintenanceResponseViewModel = ViewModelProvider(this, mainViewModelFactory).get(MaintenanceResponseViewModel::class.java)
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


        comments_txt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty() || s.length < 5) {
                    comments_Textinputlayout.error = "Please enter at least 5 characters"
                } else {
                    comments_Textinputlayout.error = null
                }
            }
        })

        // Initialize it early, maybe with an empty list
        datalistType = emptyList()
        datalistReason = emptyList()

        if (state.equals("Open")){
            spare_used.visibility = View.GONE
        } else {
            spare_used.visibility = View.VISIBLE
        }


        ticketTaskDbDataViewModel.getImagesForTicketTask(ticketId!!, taskId!!)

        txt_ticket_id.text = ticketId.toString()
        txt_ticket_name.text = title

        ticketTaskDbDataViewModel.ticketbyImages.removeObservers(requireActivity())
        imageList.clear()
        ticketTaskDbDataViewModel.ticketbyImages.observe(viewLifecycleOwner, Observer { images ->
            images?.let {
                imageList.addAll(it.map { ticketImage -> ticketImage.imageUri.split(",") }.flatten()) // Convert back to list
                uploadImagesAdapter.notifyDataSetChanged()
            }
        })

        if (state.equals("Fixed")) {
            statusfixed = "Fixed"
            mDialog = (requireActivity().application as ApplicationController)
                .showLoadingDialog(requireContext(), requireActivity().resources.getString(R.string.loading))
            mDialog?.show()
            autocomplete1_title.text = "Select the Fixed Type below :"
            autocomplete2_title.text = "Select the Fixed Reason below :"
            maintenanceResponseViewModel.fetchformdata("TroubleTicketClosedReason","Tickets", "Bearer $access_token", "null", Tenant_Id)
        } else if (state.equals("Open")) {
            statusfixed = "Open"

            mDialog = (requireActivity().application as ApplicationController)
                .showLoadingDialog(requireContext(), requireActivity().resources.getString(R.string.loading))
            mDialog?.show()
            autocomplete1_title.text = "Select the Dependency below :"
            autocomplete2_title.text = "Select the Open Reason below :"
            maintenanceResponseViewModel.fetchformdata("TroubleTicketOpenReason","Tickets", "Bearer $access_token", "null", Tenant_Id)
        }

        maintenanceResponseViewModel.FormDatalist.removeObservers(requireActivity())
        maintenanceResponseViewModel.FormDatalist.observe(viewLifecycleOwner, Observer { data ->

            if (data.errorCode.toInt() == 200){
                if (mDialog!!.isShowing && mDialog != null) {
                    mDialog!!.dismiss()
                    mDialog!!.hide()
                }

                datalistType = data.results
                dbData = false
                var arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, datalistType)
                auto_type.setAdapter(arrayAdapter)

            } else {
                if (mDialog!!.isShowing && mDialog != null) {
                    mDialog!!.dismiss()
                    mDialog!!.hide()
                }
            }

        })


        auto_type.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    auto_type.error = "This field cannot be empty"
                } else {
                    val inputText = s.toString().trim()
                    if(dbData == false) {
                        if(datalistType.isNotEmpty()) {
                            if (datalistType.isNotEmpty() && datalistType.contains(inputText)) {
                                auto_type.error = null // Clear the error if the input is valid
                            } else {
                                auto_type.error = "Invalid selection. Please choose from the list."
                            }
                        }
                    } else {
                        auto_type.error = null
                    }
                }
            }
        })


        auto_reason.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    auto_reason.error = "This field cannot be empty"
                } else {
                    val inputText = s.toString().trim()
                    if(dbData == false) {
                        if(datalistReason.isNotEmpty()) {
                            if (datalistReason.isNotEmpty() && datalistReason.contains(inputText)) {
                                auto_reason.error = null // Clear the error if the input is valid
                            } else {
                                auto_reason.error = "Invalid selection. Please choose from the list."
                            }
                        }
                    }else {
                        auto_reason.error = null
                    }
                }
            }
        })

        auto_type.setOnClickListener {
            if (auto_type.adapter != null && auto_type.adapter.count > 0) {
                auto_type.requestFocus()
                auto_type.showDropDown()
            }
        }

        auto_type.setOnFocusChangeListener { _, hasFocus ->
            isAutoTypeFocused = hasFocus
            // Show or hide the clear image based on focus and text
            updateClearImageVisibility()
        }

        auto_type.addTextChangedListener(object : TextWatcher {
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

        cleartype.setOnClickListener {
            auto_type.setText("")
        }

        auto_reason.setOnClickListener {
            if (auto_reason.adapter != null && auto_reason.adapter.count > 0) {
                auto_reason.requestFocus()
                auto_reason.showDropDown()
            }
        }

        auto_reason.setOnFocusChangeListener { _, hasFocus ->
            isAutoTypeFocused = hasFocus
            // Show or hide the clear image based on focus and text
            updateClearImageVisibilityforReason()
        }

        auto_reason.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Show or hide the clear image based on focus and text
                updateClearImageVisibilityforReason()
            }

            override fun afterTextChanged(s: Editable?) {
                // No implementation needed
            }
        })

        clearReason.setOnClickListener {
            auto_reason.setText("")
        }

        auto_type.setOnItemClickListener { parent, view, position, id ->
            val selectedText = parent.getItemAtPosition(position) as String
            auto_reason.setText("") // clearing existing entries in reason.
            if (statusfixed.equals("Fixed")) {
                maintenanceResponseViewModel.fetchReasonFormdata(
                    "TroubleTicketClosedReason",
                    selectedText,
                    "Bearer $access_token",
                    "null",
                    Tenant_Id
                )
            } else if (statusfixed.equals("Open")){
                maintenanceResponseViewModel.fetchReasonFormdata(
                    "TroubleTicketOpenReason",
                    selectedText,
                    "Bearer $access_token",
                    "null",
                    Tenant_Id
                )
            }
        }

        maintenanceResponseViewModel.FormReasonDatalist.removeObservers(requireActivity())
        maintenanceResponseViewModel.FormReasonDatalist.observe(viewLifecycleOwner, Observer { data ->
            if (data.errorCode.toInt() == 200) {

                if (mDialog!!.isShowing && mDialog != null) {
                    mDialog!!.dismiss()
                    mDialog!!.hide()
                }

                datalistReason = data.results
                dbData = false
                var arrayAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, datalistReason)
                auto_reason.setAdapter(arrayAdapter)

            } else {

                if (mDialog!!.isShowing && mDialog != null) {
                    mDialog!!.dismiss()
                    mDialog!!.hide()
                }

            }

        })

        uploadImgLayout.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(
                    requireContext() as Activity,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE)
            } else {
                // Permission has already been granted
                // Proceed to open camera
                launchImageChooserIntent()
            }
        }


        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data ?: photoUri
                imageUri?.let {
                    imageList.add(it.toString()) // Add the image URI to the list
                    uploadImagesAdapter.notifyDataSetChanged() // Notify the adapter about data change
                }
            }
        }

        uploadImagesAdapter = UploadImagesAdapter(requireContext(), imageList)
        gridView_image.setAdapter(uploadImagesAdapter)


        img_helpdesk_back.setOnClickListener{
            findNavController().navigate(R.id.action_maintenance_taskform_fragment_to_maintenance_task_list_fragment)
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
                    findNavController().navigate(R.id.action_maintenance_taskform_fragment_to_login_fragment)
                }
                builder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.setCancelable(false)
                dialog.show()
            }
        })

        radioGroup.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.radio_yes -> {
                    selectedOption = "yes"
                    myPreferences.saveString("selectedOption","yes")
                }
                R.id.radio_no -> {
                    selectedOption = "no"
                    myPreferences.saveString("selectedOption","yes")
                }
            }
        }

        btn_cancel.setOnClickListener{
            findNavController().navigate(R.id.action_maintenance_taskform_fragment_to_maintenance_task_list_fragment)
        }

        btn_continue.setOnClickListener {
            val fixedtype = auto_type.text.toString()
            val comments = comments_txt.text.toString()
            val fixedReason = auto_reason.text.toString()
            val openReason = auto_type.text.toString()
            val dependency = auto_reason.text.toString()

            if (fixedtype.isEmpty() || openReason.isEmpty()) {
                auto_type.error = "Please select a type"
                return@setOnClickListener
            }

            if (fixedReason.isEmpty() || dependency.isEmpty()) {
                auto_reason.error = "Please select a reason"
                return@setOnClickListener
            }

            if (comments.isEmpty() || comments.length < 5) {
                comments_Textinputlayout.error = "Please enter at least 5 characters"
                return@setOnClickListener
            }

            if (state.equals("Fixed")){
                isSpareIndentUsed = selectedOption
            } else {
                isSpareIndentUsed = ""
            }

            GlobalScope.launch(Dispatchers.IO) {
                val existingTickets = userDao.getTaskByTicketIdTaskId(ticketId!!, taskId!!)
                if (existingTickets.isEmpty() == false) {
                    ticketTaskDbDataViewModel.updateTicket(
                        ticketId!!,
                        taskId!!,
                        state,
                        comments,
                        isSpareIndentUsed,
                        if (state == "Fixed") fixedtype else "",
                        if (state == "Fixed") fixedReason else "",
                        if (state == "Open") openReason else "",
                        if (state == "Open") dependency else "",
                        title
                    )
                }

                for (imageUri in imageList) {
                    ticketTaskDbDataViewModel.addImageToTickets(ticketId!!, taskId!!, imageUri)
                }
            }


            var lastTask = myPreferences.getBoolean("lastTask", false)
            when {
                state.equals("Fixed") -> {
                    if (selectedOption == "yes") {
                        Toast.makeText(requireContext(), "Task details saved successfully", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_maintenance_taskform_fragment_to_spare_details_fragment)
                    } else {
                        if (lastTask) {
                            Toast.makeText(requireContext(), "Task details saved successfully", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_maintenance_taskform_fragment_to_summary_details_fragment)
                        } else {
                            showConfirmationDialog {
                                findNavController().navigate(R.id.action_maintenance_taskform_fragment_to_maintenance_task_list_fragment)
                            }
                        }
                    }
                }

                state.equals("Open") -> {
                    if (auto_type.text.toString().equals("Spare Parts", ignoreCase = true)) {
                        myPreferences.saveString("selectedOption", "Spare Parts")
                        Toast.makeText(requireContext(), "Task details saved successfully", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_maintenance_taskform_fragment_to_raise_indent_request)
                    } else {
                        myPreferences.saveString("selectedOption", "Open")
                        if (lastTask) {
                            Toast.makeText(requireContext(), "Task details saved successfully", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_maintenance_taskform_fragment_to_summary_details_fragment)
                        } else {
                            showConfirmationDialog {
                                findNavController().navigate(R.id.action_maintenance_taskform_fragment_to_maintenance_task_list_fragment)
                            }
                        }
                    }
                }
            }
        }
        // Fetch necessary data from SharedPreferences
        fetchAndUpdateTaskDetailsFromDatabase()

        return view
    }


    private fun showConfirmationDialog(onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext(),R.style.CustomAlertDialogTheme)
            .setMessage("Task saved successfully, proceed with remaining tasks.")
            .setPositiveButton("OK") { _, _ ->
                onConfirm()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun fetchAndUpdateTaskDetailsFromDatabase() {

        // Check if the ticket already exists
        lifecycleScope.launch {
            // Fetch task details from the database based on the ticketId and taskId
            ticketTaskDbDataViewModel.getTaskByTicketIdTaskId(ticketId!!, taskId!!)
            ticketTaskDbDataViewModel.taskData.removeObservers(requireActivity())
            ticketTaskDbDataViewModel.taskData.observe(viewLifecycleOwner, Observer { ticket ->
                ticket?.let {
                    // If the ticket does not exist, insert it
                    val taskEntity = TaskEntity(
                        ticketId = ticketId!!,
                        ticketTaskId = taskId!!,
                        taskstatus = ticket.get(0).taskstatus,
                        comments = ticket.get(0).comments,
                        isSpareIndentUsed = "",
                        fixedtype = ticket.get(0).fixedtype,
                        fixedReason = ticket.get(0).fixedReason,
                        openReason = ticket.get(0).openReason,
                        dependency = ticket.get(0).dependency,
                        description = ticket.get(0).description
                    )
                        if(ticket.get(0).comments.isNotBlank() && (ticket.get(0).fixedReason!!.isNotBlank() || ticket.get(0).dependency!!.isNotBlank())
                                                               && ticket.get(0).comments.isNotBlank()) {
                            populateUIFields(taskEntity)
                        }
                    }
                })
        }
    }

    private fun populateUIFields(ticket: TaskEntity) {
        // Update the comments field
        comments_txt.setText(ticket.comments)
        dbData = true
        // Update the type and reason fields based on task status
        if (ticket.taskstatus == "Fixed") {
            auto_type.setText(ticket.fixedtype)
            auto_reason.setText(ticket.fixedReason)
        } else if (ticket.taskstatus == "Open") {
            auto_type.setText(ticket.openReason)
            auto_reason.setText(ticket.dependency)
        }

        // Update the radio button based on the isSpareIndentUsed field
        when (ticket.isSpareIndentUsed) {
            "yes" -> radio_yes.isChecked = true
            "no" -> radio_no.isChecked = true
            else -> radioGroup.clearCheck()
        }
    }


    fun showCustomErrorDialog(msg:String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_custom_alert, null)
        val alertDialog = AlertDialog.Builder(requireContext(),R.style.CustomAlertDialogTheme)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ -> }
            .create()

        // Set the message text dynamically
        val messageTextView = dialogView.findViewById<TextView>(R.id.message)
        messageTextView.text = msg

        alertDialog.show()
    }

    fun updateClearImageVisibility() {
        if (isAutoTypeFocused && !auto_type.text.isNullOrEmpty()) {
            cleartype.visibility = View.VISIBLE
        } else {
            cleartype.visibility = View.GONE
        }
        dbData = false
    }

    fun updateClearImageVisibilityforReason() {
        if (isAutoTypeFocused && !auto_reason.text.isNullOrEmpty()) {
            clearReason.visibility = View.VISIBLE
        } else {
            clearReason.visibility = View.GONE
        }
        dbData = false
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, proceed to open camera
                    launchImageChooserIntent()
                } else {
                    // Permission denied, handle accordingly
                    Toast.makeText(requireContext(),"Permission denied",Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun launchImageChooserIntent() {
        //  currentTicketId = ticketId
        val chooseImageIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoUri = createImageFileUri()
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

        val intentChooser = Intent.createChooser(chooseImageIntent, "Select Image Source").apply {
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        }

        activityResultLauncher.launch(intentChooser)
    }

    @Throws(IOException::class)
    private fun createImageFileUri(): Uri? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            imageFile
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing
                findNavController().navigate(R.id.action_maintenance_taskform_fragment_to_maintenance_task_list_fragment)

            }
        })
    }

}