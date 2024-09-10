package com.example.newwowzkidsworld.util

object User {
    var userId: Int? = null
    var name: String? = null
    var mobile: String? = null

    fun updateUser(userId: Int, name: String, mobile: String) {
        this.userId = userId
        this.name = name
        this.mobile = mobile
    }

    fun clearUser() {
        this.userId = null
        this.name = null
        this.mobile = null
    }
}
