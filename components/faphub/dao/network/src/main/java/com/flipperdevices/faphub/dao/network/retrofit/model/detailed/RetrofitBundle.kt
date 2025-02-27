package com.flipperdevices.faphub.dao.network.retrofit.model.detailed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RetrofitBundle(
    @SerialName("_id") val id: String,
    @SerialName("filename") val filename: String,
    @SerialName("length") val length: Long
)
