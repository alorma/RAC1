package com.alorma.rac.data.api

import com.google.gson.annotations.SerializedName

data class NowApiModel(
    @SerializedName("program") val programApiModel: ProgramApiModel
)

class NowResponse : BaseResponse<NowApiModel>()