package com.alorma.rac.data.api

data class NowApiModel(val programApiModel: ProgramApiModel)

class NowResponse : BaseResponse<NowApiModel>()