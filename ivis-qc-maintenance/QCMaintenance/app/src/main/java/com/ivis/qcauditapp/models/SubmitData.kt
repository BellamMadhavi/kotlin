package com.qcauditapp.models

import androidx.room.Entity

import android.os.Parcel
import android.os.Parcelable

data class SubmitData(
    val id: String,
    val value: String,
    val type: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(value)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubmitData> {
        override fun createFromParcel(parcel: Parcel): SubmitData {
            return SubmitData(parcel)
        }

        override fun newArray(size: Int): Array<SubmitData?> {
            return arrayOfNulls(size)
        }
    }
}
