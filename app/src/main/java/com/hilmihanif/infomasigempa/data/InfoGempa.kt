package com.hilmihanif.infomasigempa.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InfoGempa(
    val gempa: List<Gempa>
):Parcelable
