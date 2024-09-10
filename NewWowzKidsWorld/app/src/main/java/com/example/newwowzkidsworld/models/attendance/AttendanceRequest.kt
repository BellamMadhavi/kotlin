package com.example.newwowzkidsworld.models.attendance

data class AttendanceRequest(
    val action: String,
    val attendance_data: List<AttendancePostRecord>
)
