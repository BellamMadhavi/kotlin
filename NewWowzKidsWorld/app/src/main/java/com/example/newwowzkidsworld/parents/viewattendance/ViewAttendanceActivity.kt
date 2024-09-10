package com.example.newwowzkidsworld.parents.viewattendance

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.newwowzkidsworld.models.attendance.AttendanceSingleRecord
import com.example.newwowzkidsworld.retrofit.retrofit.RetrofitClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*

class ViewAttendanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViewAttendanceScreen()
        }
    }
}

@Composable
fun ViewAttendanceScreen() {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val context = LocalContext.current
    val studentId = 2 // Replace with actual student ID
    var attendanceSingleRecords by remember { mutableStateOf<List<AttendanceSingleRecord>>(emptyList()) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Select Date:")
        val calendar = Calendar.getInstance()

        var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH) + 1) }
        var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
        var selectedDate by remember { mutableStateOf("") }

        // Fetch attendance records
        LaunchedEffect(selectedMonth, selectedYear) {
            try {
                attendanceSingleRecords = RetrofitClient.instance.getMonthlyAttendance(studentId, selectedMonth, selectedYear)
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load data: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        // Display the custom calendar
        CustomCalendarView(
            year = selectedYear,
            month = selectedMonth,
            attendanceRecords = attendanceSingleRecords,
            onDateSelected = { date ->
                selectedDate = date
                Toast.makeText(context, "Selected Date: $date", Toast.LENGTH_SHORT).show()
            }
        )

        if (selectedDate.isNotEmpty()) {
            Text("Selected Date: $selectedDate")
        }
    }
}
