package com.example.sharedpreference

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etMobile: EditText
    private lateinit var etPin: EditText
    private lateinit var btnLogin: Button
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize UI components and SharedPreferences helper
        etMobile = findViewById(R.id.et_mobile)
        etPin = findViewById(R.id.et_pin)
        btnLogin = findViewById(R.id.btn_login)
        preferenceHelper = PreferenceHelper(this)

        // Check if user details already exist in SharedPreferences
        if (isUserLoggedIn()) {
            // If user is already logged in, navigate to WelcomeActivity
            navigateToWelcomeScreen()
        }

        btnLogin.setOnClickListener {
            val mobile = etMobile.text.toString()
            val pin = etPin.text.toString()

            if (mobile.isNotEmpty() && pin.isNotEmpty()) {
                loginUser(mobile, pin)
            } else {
                Toast.makeText(this, "Please enter mobile and pin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to check if user is already logged in
    private fun isUserLoggedIn(): Boolean {
        val userName = preferenceHelper.getUserName()
        val userMobile = preferenceHelper.getUserMobile()
        return userName != null && userMobile != null
    }

    // Function to navigate to WelcomeActivity
    private fun navigateToWelcomeScreen() {
        startActivity(Intent(this@LoginActivity, WelcomeActivity::class.java))
        finish() // Close the LoginActivity so user cannot go back to it
    }

    // Function to handle user login
    private fun loginUser(mobile: String, pin: String) {
        val call = RetrofitClient.instance.login(mobile, pin)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val user = response.body()?.user
                    if (user != null) {
                        // Save user details in SharedPreferences
                        preferenceHelper.saveUser(user.name, user.mobile)
                        Toast.makeText(this@LoginActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        navigateToWelcomeScreen()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginActivity", "Error: ${t.message}")
                Toast.makeText(this@LoginActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
