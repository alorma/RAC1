package com.alorma.rac1.data.net

import com.google.gson.annotations.SerializedName

data class ResponseProgramsDto(@SerializedName("result") val result: List<ProgramDto>)
data class ResponseScheduleDto(@SerializedName("result") val result: List<WrappedProgram>)
data class ResponseNowDto(@SerializedName("result") val result: WrappedProgram)

data class WrappedProgram(@SerializedName("program") val program: ProgramDto,
                          @SerializedName("start") val start: String,
                          @SerializedName("end") val end: String,
                          @SerializedName("duration") val duration: String)