package com.qcauditapp.retrofit

import com.ivis.qcauditapp.models.ApiResponse
import com.ivis.qcauditapp.models.IndentSpareDetails
import com.ivis.qcauditapp.models.LoginRequest
import com.ivis.qcauditapp.models.MaintenanceResponse
import com.ivis.qcauditapp.models.MaintenancefrreportsData
import com.ivis.qcauditapp.models.RequestSpareItem
import com.ivis.qcauditapp.models.TicketDetailsResponse
import com.ivis.qcauditapp.models.TicketDetailsSummary
import com.ivis.qcauditapp.models.TicketDetailsSummaryResponse
import com.ivis.qcauditapp.models.TicketResponse
import com.ivis.qcauditapp.models.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface API {
        @POST("login")
        suspend fun getlogin(
            @Body loginRequest: LoginRequest
        ): Response<ApiResponse>
        //login API

        @GET("ticket-audits/userscheduleList")
        suspend fun getSchedules(
            @Query("userId") userId: Int,
            @Query("scheduleDate") scheduleDate: String,
            @Header("Authorization") authorization: String,
            @Header("Customer-Id") Customer_Id: String,
            @Header("Tenant-Id") Tenant_Id: Int
        ): Response<TicketResponse>
        //Schedules API

        @GET("ticket-audits/fr-reports")
        suspend fun getreports(
            @Query("userId") userId: Int,
            @Header("Authorization") authorization: String,
            @Header("Customer-Id") Customer_Id: String,
            @Header("Tenant-Id") Tenant_Id: Int
        ): Response<MaintenancefrreportsData>


            @GET("lookupvalues")
            suspend fun getreasons(
                @Query("type") type: String,
                @Query("category") category: String,
                @Header("Authorization") authorization: String,
                @Header("Customer-Id") Customer_Id: String,
                @Header("Tenant-Id") Tenant_Id: Int
            ): Response<MaintenanceResponse>

        @GET("trouble-tickets/getTicketInfoByTicketId")
        suspend fun getTicketDetails(
            @Query("ticketId") ticketId: Int?,
            @Header("Authorization") authorization: String,
            @Header("Customer-Id") Customer_Id: String,
            @Header("Tenant-Id") Tenant_Id: Int
        ): Response<TicketDetailsResponse>


        @GET("ticket-audits/fr-spares-indents")
        suspend fun getIndentDetails(
            @Query("ticketId") ticketId: Int,
            @Header("Authorization") authorization: String,
            @Header("Customer-Id") Customer_Id: String,
            @Header("Tenant-Id") Tenant_Id: Int
        ): Response<IndentSpareDetails>

        @GET("dropdown/spare-items")
        suspend fun getSpareRequests(
            @Header("Authorization") authorization: String,
            @Header("Customer-Id") Customer_Id: String,
            @Header("Tenant-Id") Tenant_Id: Int
        ): Response<RequestSpareItem>

        @PUT("ticket-audits/submit-schedule-visit")
        suspend fun SubmitSummaryTicketDetails(
            @Header("Authorization") authorization: String,
            @Header("Customer-Id") customerId: String,
            @Header("Tenant-Id") tenantId: Int,
            @Body ticketDetailsSummaryRequestBody: RequestBody
        ): Response<TicketDetailsSummaryResponse>

}
