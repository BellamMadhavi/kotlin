package com.ivis.qcauditapp.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.ivis.qcauditapp.utils.NetworkUtils
import com.qcauditapp.ApplicationController
import com.qcauditapp.R
import com.qcauditapp.viewmodels.MainViewModel
import com.qcauditapp.viewmodels.MainViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val connectivityReceiver = ConnectivityReceiver()

    private var currentFragmentId = 0
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
        registerConnectivityReceiver()
    }

    override fun onPause() {
        super.onPause()
        unregisterConnectivityReceiver()
    }

    private fun registerConnectivityReceiver() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, filter)
    }

    private fun unregisterConnectivityReceiver() {
        unregisterReceiver(connectivityReceiver)
    }

    private class ConnectivityReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

            if (isConnected) {
                Log.d("Networkmani", "Connected")
              //  showSnackbar(context,"Connected")
            } else {
                Log.d("Networkmani", "Not connected")
                showSnackbar(context,"Not connected")
            }
        }
        private fun showSnackbar(context: Context, message: String) {
            val rootView: View? = (context as? AppCompatActivity)?.findViewById(android.R.id.content)
            rootView?.let {
                val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }
    }

}



















