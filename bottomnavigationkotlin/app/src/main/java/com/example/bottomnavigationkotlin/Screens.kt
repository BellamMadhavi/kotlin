package com.example.bottomnavigationkotlin

sealed class Screens(val screen: String) {
    object Home : Screens("home")
    object Dashboard : Screens("dashboard")
    object Account : Screens("account")
}
