package com.ivis.qcauditapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UnitIdResponse
    (
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("deviceId")
    @Expose
    var deviceId: String? = null,

    @SerializedName("siteName")
    @Expose
    var siteName: String? = null,

    @SerializedName("facility")
    @Expose
    var facility: String? = null
)