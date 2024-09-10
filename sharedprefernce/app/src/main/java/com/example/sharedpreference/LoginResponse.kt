package com.example.sharedpreference

data class LoginResponse(
    val success: Boolean,
    val user: User?,
    val message: String
)

data class User(
    val userId: Int,
    val name: String,
    val mobile: String
)
