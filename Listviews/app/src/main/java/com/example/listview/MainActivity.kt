package com.example.listview

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.listview.ui.theme.ListViewTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    private lateinit var textViewResponse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.retrofit) // Ensure 'retrofit.xml' is correctly set up with a TextView having id 'textViewResponse'

        textViewResponse = findViewById(R.id.textViewResponse)

        // Initiate login process
        login()
        //Button
//        setContentView(R.layout.main_activity)
//
//        val btn = findViewById<Button>(R.id.btncool)
//        val btn1 = findViewById<Button>(R.id.btnhot)
//        val linearLayout = findViewById<LinearLayout>(R.id.linearlayout)
//
//        btn.setOnClickListener {
//            linearLayout.setBackgroundColor(R.color.blue)
//        }
//        btn1.setOnClickListener {
//            linearLayout.setBackgroundColor(R.color.black)
//        }


//ListView
//        setContentView(R.layout.listview)
//        val listView: ListView = findViewById<ListView>(R.id.listView)
//
//        // Sample data to display in the ListView
//        val items = listOf("Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grape")
//
//        // Create an ArrayAdapter to bind the data to the ListView
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
//
//        // Set the adapter to the ListView
//        listView.adapter = adapter


    }
    private fun login() {
        val loginRequest = LoginRequest(
            loginId = "yaswanth.a",
            password = "Ivis@123"
        )

        RetrofitClient.apiService.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    textViewResponse.text = "First Name: ${loginResponse?.results?.firstName}"
                } else {
                    textViewResponse.text = "Error: ${response.code()} - ${response.message()}"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                textViewResponse.text = "Failure: ${t.message}"
            }
        })


    }


}