package com.alorma.rac1.net

import com.google.gson.annotations.SerializedName

data class ImagesDto(
        @SerializedName("app") val app: String,
        @SerializedName("itunes") val itunes: String,
        @SerializedName("share") val share: String,
        @SerializedName("program") val program: String,
        @SerializedName("person") val person: String,
        @SerializedName("person-small") val personSmall: String,
        @SerializedName("podcast") val podcast: String
)