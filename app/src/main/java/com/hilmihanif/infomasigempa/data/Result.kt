package com.hilmihanif.infomasigempa.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


data class Result(
    @SerializedName("Infogempa")
    val infoGempa: InfoGempa
)