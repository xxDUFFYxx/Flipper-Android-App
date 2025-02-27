package com.flipperdevices.faphub.dao.network.retrofit.model.detailed

import com.flipperdevices.core.data.SemVer
import com.flipperdevices.faphub.dao.api.model.FapCategory
import com.flipperdevices.faphub.dao.api.model.FapDeveloperInformation
import com.flipperdevices.faphub.dao.api.model.FapItem
import com.flipperdevices.faphub.dao.api.model.FapItemVersion
import com.flipperdevices.faphub.dao.api.model.FapMetaInformation
import com.flipperdevices.faphub.dao.network.retrofit.utils.DateSerializer
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RetrofitApplicationDetailed(
    @SerialName("_id") val id: String,
    @SerialName("created_at")
    @Serializable(with = DateSerializer::class)
    val createdAt: LocalDateTime,
    @SerialName("updated_at")
    @Serializable(with = DateSerializer::class)
    val updatedAt: LocalDateTime,
    @SerialName("category_id") val categoryId: String,
    @SerialName("alias") val alias: String,
    @SerialName("name") val name: String,
    @SerialName("current_version") val currentVersion: RetrofitVersionDetailed,
) {
    fun toFapItem(category: FapCategory): FapItem {
        val picUrl = currentVersion.iconUrl

        val fapDeveloperInformation = FapDeveloperInformation(
            githubRepositoryLink = currentVersion.links.sourceCode.url,
            manifestRepositoryLink = currentVersion.links.manifestUrl
        )

        val metaInformation = FapMetaInformation(
            version = SemVer.fromString(currentVersion.version)
                ?: error("Failed parse ${currentVersion.version}"),
            sizeBytes = currentVersion.bundle.length,
            apiVersion = SemVer.fromString(currentVersion.currentBuild.sdk.api)
                ?: error("Failed parse ${currentVersion.version}")
        )

        val fapItemVersion = FapItemVersion(
            id = currentVersion.id,
            version = SemVer.fromString(currentVersion.version)
                ?: error("Can't parse ${currentVersion.version}")
        )

        return FapItem(
            id = id,
            screenshots = currentVersion.screenshots.toImmutableList(),
            description = currentVersion.description,
            name = name,
            changelog = currentVersion.changelog,
            category = category,
            picUrl = picUrl,
            metaInformation = metaInformation,
            fapDeveloperInformation = fapDeveloperInformation,
            applicationId = alias,
            upToDateVersion = fapItemVersion
        )
    }
}
