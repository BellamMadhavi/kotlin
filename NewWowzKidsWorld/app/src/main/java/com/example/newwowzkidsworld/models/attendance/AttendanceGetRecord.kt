package com.example.newwowzkidsworld.models.attendance

data class AttendanceGetRecord(
    val roll_no:Int,
    val student_name:String,
    val date: String,
    val status: String,
    val subject: String,
    val comment: String,
    val gender: String
)

