package com.alorma.rac1.data.net

import com.google.gson.annotations.SerializedName

data class ProgramDto(
        @SerializedName("id") val id: String,
        @SerializedName("title") val title: String,
        @SerializedName("subtitle") val subtitle: String,
        @SerializedName("description") val description: String,
        @SerializedName("schedule") val schedule: String,
        @SerializedName("socialNetworks") val socialNetworks: SocialNetworksDto,
        @SerializedName("images") val images: ImagesDto,
        @SerializedName("url") val url: String,
        @SerializedName("active") val active: Boolean,
        @SerializedName("hidden") val hidden: Boolean
)