package com.example.listview

data class LoginResponse(
    val results: Results?,
    val errorMessage: String?,
    val errorCode: String?
)

data class Results(
    val userId: Int,
    val userType: String,
    val category: String,
    val accessToken: String,
    val tenantId: Any?,
    val refreshToken: String,
    val fullName: String,
    val employeeId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String,
    val departmentName: String,
    val mappedCustomers: List<MappedCustomer>,
    val mappedTenants: List<Any>
)

data class MappedCustomer(
    val pkCustomerId: Int,
    val customerName: String,
    val fkTenantId: Int,
    val customerCode: String,
    val mappedGroups: Any?
)




