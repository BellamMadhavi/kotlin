package com.example.newwowzkidsworld.teacher.postassignments

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiServiceFileUpload {
    @Multipart
    @POST("upload_assignment.php")
    suspend fun uploadAssignment(
        @Part("teacher_id") teacherId: RequestBody,
        @Part("class") className: RequestBody,
        @Part("homework") homework: RequestBody,
        @Part("due_date") dueDate: RequestBody,
        @Part file: MultipartBody.Part
    ): ApiResponse
}
