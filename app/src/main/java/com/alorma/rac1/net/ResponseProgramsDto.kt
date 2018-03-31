package com.alorma.rac1.net

import com.google.gson.annotations.SerializedName

data class ResponseProgramsDto(@SerializedName("result") val result: List<ProgramDto>)
data class ResponseScheduleDto(@SerializedName("result") val result: List<WrappedProgram>)
data class ResponseNowDto(@SerializedName("result") val result: WrappedProgram)

data class WrappedProgram(@SerializedName("program") val program: ProgramDto)