package com.example.newwowzkidsworld.retrofit.retrofit
import com.example.newwowzkidsworld.models.attendance.AttendanceGetRecord
import com.example.newwowzkidsworld.models.attendance.AttendancePostRecord
import com.example.newwowzkidsworld.models.attendance.AttendanceRequest
import com.example.newwowzkidsworld.models.attendance.AttendanceSingleRecord
import com.example.newwowzkidsworld.models.attendance.Student
import com.example.newwowzkidsworld.models.login.LoginRequest
import com.example.newwowzkidsworld.models.login.LoginResponse
import com.example.newwowzkidsworld.teacher.postassignments.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("login/login.php")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>


    @Headers("Content-Type: application/json")
    @POST("attendance/attendance.php")
    suspend fun postAttendance(@Body attendanceRequest: AttendanceRequest): ApiResponse

    @GET("attendance/attendance.php")
    suspend fun getStudentsAttendanceForTakingAttendance(
        @Query("class") class1: String,
        @Query("section") section: String,
        @Query("date") date: String // Include date parameter
    ): List<Student>

    @FormUrlEncoded
    @POST("attendance/updateAttendanceComment.php")
    suspend fun updateAttendanceComment(
        @Field("student_id") studentId: Int,
        @Field("comment") comment: String,
        @Field("date") date: String
    ): Response<ResponseBody>

    @GET("fetch_monthly_attendance.php")
    suspend fun getMonthlyAttendance(
        @Query("student_id") studentId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): List<AttendanceSingleRecord>

    @GET("attendance/fetch_attendance.php")
    suspend fun getAttendanceExistingRecords(
        @Query("class") class1: String,
        @Query("section") section: String,
        @Query("subject") subject: String,
        @Query("date") date: String
    ): List<AttendanceGetRecord>
}