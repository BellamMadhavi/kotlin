package com.ivis.qcauditapp.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.net.Network
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ivis.qcauditapp.db.UserDao
import com.ivis.qcauditapp.models.User
import com.ivis.qcauditapp.utils.NetworkUtils.Companion.isInternetAvailable
import com.ivis.qcauditapp.utils.QcPreferences
import com.qcauditapp.ApplicationController
import com.qcauditapp.BuildConfig
import com.qcauditapp.R
import com.qcauditapp.viewmodels.MainViewModel
import com.qcauditapp.viewmodels.MainViewModelFactory
import javax.inject.Inject


class LoginFragment : Fragment() {
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var btnLogin: Button
    private var mDialog: Dialog? = null

    private lateinit var userDao: UserDao

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_login, container, false)

        username = view.findViewById(R.id.user_name)
        password = view.findViewById(R.id.edtxtPwd)
        btnLogin = view.findViewById(R.id.btnLogin)

        val myPreferences = QcPreferences(requireContext())

        (requireActivity().application as ApplicationController).applicationComponent.inject(this)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        // Get an instance of the Room database and User DAO

        btnLogin.setOnClickListener {
            var username = username.text.toString().trim()
            var password = password.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                if (isInternetAvailable(requireContext())) {
                    mainViewModel.fetch_login_data(username.toString().trim(), password.toString().trim())
                } else {
                    showErrorDialog("No internet connection. Please check your internet settings and try again.")
                }
            } else {
                Toast.makeText(requireContext(), "Enter the username and password", Toast.LENGTH_SHORT).show()
            }
        }


        username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    val newText = it.toString().replace(" ", "")
                    if (it.toString() != newText) {
                        username.setText(newText)
                        username.setSelection(newText.length)  // Move cursor to the end
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        username.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                password.requestFocus()
                true
            } else {
                false
            }
        }

        mainViewModel.accountdata.removeObservers(requireActivity())
        mainViewModel.accountdata.observe(requireActivity(), Observer { response ->
            response?.let {
                if (it.results != null) {
                    // Handle successful login scenario
                    handleSuccessfulLogin(it.results)
                }
            }
        })
        mainViewModel.errordata.removeObservers(requireActivity())
        mainViewModel.errordata.observe(requireActivity(), Observer { response ->
            showErrorDialog(response)
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the TextView
        val versionTextView: TextView = view.findViewById(R.id.txt_version)

        // Get the version name from BuildConfig
        val versionName = BuildConfig.VERSION_NAME

        // Set the version name to the TextView
        versionTextView.text = "Version: $versionName"

    }

    private fun handleSuccessfulLogin(user: User) {
        // Save user data to preferences if needed
        val myPreferences = QcPreferences(requireContext())
        myPreferences.saveString("UserName", user.username)
        myPreferences.saveString("firstName", user.firstName)
        myPreferences.saveString("lastName", user.lastName)
        myPreferences.saveInt("userId", user.userId)
        myPreferences.saveString("accessToken", user.accessToken)
        myPreferences.saveString("refreshToken", user.refreshToken)
        myPreferences.saveInt("Tenant-Id", user.tenantId)

        // Handle navigation to DashboardFragment or other actions
        findNavController().navigate(R.id.action_LoginFragment_to_DashboardFragment)
    }

    private fun showErrorDialog(errMsg:String) {
        val builder = AlertDialog.Builder(requireContext(),R.style.CustomAlertDialogTheme)
        builder.setTitle("Error")
        builder.setMessage(errMsg)
        builder.setPositiveButton("Ok") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
    }
}


