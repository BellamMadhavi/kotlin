package com.ivis.qcauditapp.models

data class ApiResponse(
    val results: User?,
    val errorMessage: String?,
    val errorCode: String?
)

data class User(
    val userId: Int,
    val userType: String,
    val category: String,
    val accessToken: String,
    val refreshToken: String,
    val tenantId: Int,
    val mappedScopes: List<String>,
    val mappedSegments: List<String>,
    val fullName: String?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String,
    val theme: String?,
    val mobilePhone: String,
    val departmentName: String,
    val mappedCustomers: List<MappedCustomer>,
    val mappedTenants: List<MappedTenant>,
    val mappedGroups: List<MappedGroup>,
    val projectName: String?,
    val cportalResponse: CportalResponse
)

data class MappedCustomer(
    val pkCustomerId: Int,
    val customerName: String,
    val fkTenantId: Int,
    val mappedGroups: Any? // Replace with actual type if needed
)

data class MappedTenant(
    val pkTenantId: Int,
    val tenantName: String,
    val mappedGroups: Any? // Replace with actual type if needed
)

data class MappedGroup(
    val userId: Int,
    val userType: String,
    val tenantId: Int,
    val tenantName: String,
    val title: String
)

data class CportalResponse(
    val userId: Int,
    val userType: String,
    val category: String,
    val accessToken: String,
    val refreshToken: String,
    val fullName: String?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String,
    val departmentName: String,
    val mappedCustomers: List<Any> // Replace with actual type if needed
)

