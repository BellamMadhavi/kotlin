package com.example.newwowzkidsworld.models.login

data class LoginResponse(val success: Boolean, val user: UserData?, val message: String)

data class UserData(val userId: Int, val name: String, val mobile: String)
