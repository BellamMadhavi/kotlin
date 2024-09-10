package com.example.newwowzkidsworld.models.attendance

data class AttendancePostRecord(
    val student_id: Int,
    val roll_no:Int,
    val name:String,
    val date: String,
    val status: String,
    val teacher_id: String,
    val subject: String,
    val class_id: Int
)
