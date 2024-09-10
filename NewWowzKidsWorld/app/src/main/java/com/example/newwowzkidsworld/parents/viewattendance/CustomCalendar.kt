package com.example.newwowzkidsworld.parents.viewattendance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.newwowzkidsworld.models.attendance.AttendanceSingleRecord
import java.util.*

@Composable
fun CustomCalendarView(year: Int, month: Int, attendanceRecords: List<AttendanceSingleRecord>, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, 1) // Set to the first day of the given month
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    Column {
        for (week in 0 until (daysInMonth / 7 + 1)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                for (day in 1..7) {
                    val currentDay = week * 7 + day
                    if (currentDay <= daysInMonth) {
                        val date = String.format("%04d-%02d-%02d", year, month, currentDay)
                        val attendanceRecord = attendanceRecords.find { it.date == date }
                        CalendarDayView(currentDay, attendanceRecord?.status) {
                            onDateSelected(date)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarDayView(day: Int, status: String?, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .padding(4.dp)
        .clickable(onClick = onClick)) {
        Text(day.toString())
        Spacer(modifier = Modifier.height(4.dp))
        if (status != null) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (status == "present") Color.Green else Color.Red
                    )
            )
        }
    }
}
