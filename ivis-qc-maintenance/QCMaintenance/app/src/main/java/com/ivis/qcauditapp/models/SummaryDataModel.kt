package com.ivis.qcauditapp.models

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody

data class BasicInformation(
    @SerializedName("User Name") val userName: String,
    @SerializedName("ID") val id: String,
    @SerializedName("Device ID") val deviceId: String,
    @SerializedName("Site Name") val siteName: String,
    @SerializedName("Ticket id") val ticketId: String,
    @SerializedName("QC Type") val qctype: String
)

data class TwowayCallItem(
    val value: String,
    val type: String
)

data class SensorItem(
    val value: String,
    val type: String,
    @SerializedName("Trigger Time") val triggerTime: String
)

data class AudioItem(
    val value: String,
    val type: String
)

data class SummaryDataModel(
    @SerializedName("Basic Information") val basicInformation: List<BasicInformation>,
    @SerializedName("TwowayCallList") val twowayCallList: List<TwowayCallItem>,
    @SerializedName("SensorsList") val sensorsList: List<SensorItem>,
    @SerializedName("AudioList") val audioList: List<AudioItem>
) {
    private var pdfResponseBody: ResponseBody? = null

    fun setPdfResponseBody(responseBody: ResponseBody) {
        pdfResponseBody = responseBody
    }

    fun getPdfResponseBody(): ResponseBody? {
        return pdfResponseBody
    }
}
