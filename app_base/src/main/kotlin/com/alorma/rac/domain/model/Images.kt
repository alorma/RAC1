package com.alorma.rac.domain.model

import com.google.gson.annotations.SerializedName

data class Images(
    val app: String?,
    val itunes: String?,
    val share: String?,
    val program: String?,
    val person: String?,
    @SerializedName("person-small")
    val personSmall: String?,
    val podcast: String?
)