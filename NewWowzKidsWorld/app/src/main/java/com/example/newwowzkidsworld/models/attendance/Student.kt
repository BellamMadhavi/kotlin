package com.example.newwowzkidsworld.models.attendance

data class Student(
    val id: Int,
    val roll_no: Int,
    val student_name: String,
    val class_id: Int,
    var status: String?,
    val mobile: String?,
    val comment:String?,
    val gender:String?
)
