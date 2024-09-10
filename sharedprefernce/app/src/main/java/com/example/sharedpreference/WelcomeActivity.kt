package com.example.sharedpreference

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        tvWelcome = findViewById(R.id.tv_welcome)
        preferenceHelper = PreferenceHelper(this)

        val userName = preferenceHelper.getUserName()
        tvWelcome.text = "Welcome, $userName!"
    }
}
