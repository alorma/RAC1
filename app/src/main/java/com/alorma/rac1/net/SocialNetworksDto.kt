package com.alorma.rac1.net

import com.google.gson.annotations.SerializedName

data class SocialNetworksDto(
        @SerializedName("facebook") val facebook: String,
        @SerializedName("twitter") val twitter: String,
        @SerializedName("instagram") val instagram: String
)