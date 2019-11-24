package com.alorma.rac.data.api

import com.google.gson.annotations.SerializedName

data class ImagesApiModel(
    val app: String?,
    val itunes: String?,
    val share: String?,
    val program: String?,
    val person: String?,
    @SerializedName("person-small")
    val personSmall: String?,
    val podcast: String?
)