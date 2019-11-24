package com.alorma.rac.data.api

import com.google.gson.annotations.SerializedName

data class ProgramApiModel(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subtitle: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("schedule") val schedule: String?,
    @SerializedName("socialNetworks") val socialNetworks: SocialNetworksApiModel?,
    @SerializedName("images") val images: ImagesApiModel?,
    @SerializedName("email") val email: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("sections") val sections: List<SectionApiModel> = emptyList()
)