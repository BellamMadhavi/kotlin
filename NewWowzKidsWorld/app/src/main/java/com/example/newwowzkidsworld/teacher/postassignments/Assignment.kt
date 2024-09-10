package com.example.newwowzkidsworld.teacher.postassignments

data class Assignment(
    val teacher_id: Int,
    val `class`: String,
    val homework: String,
    val due_date: String
)

data class ApiResponse(val message: String, val file_url: String)
