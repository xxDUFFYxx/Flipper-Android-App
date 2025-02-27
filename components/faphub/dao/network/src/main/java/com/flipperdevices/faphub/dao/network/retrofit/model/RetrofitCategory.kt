package com.flipperdevices.faphub.dao.network.retrofit.model

import com.flipperdevices.faphub.dao.api.model.FapCategory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RetrofitCategory(
    @SerialName("_id") val id: String,
    @SerialName("priority") val priority: Int,
    @SerialName("name") val name: String,
    @SerialName("color") val color: String,
    @SerialName("icon_uri") val iconUrl: String,
    @SerialName("applications") val applicationsCount: Int
) {
    fun toFapCategory(): FapCategory {
        return FapCategory(
            id = id,
            name = name,
            picUrl = iconUrl,
            applicationCount = applicationsCount
        )
    }
}
