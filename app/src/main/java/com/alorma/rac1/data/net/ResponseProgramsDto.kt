package com.alorma.rac1.data.net

import com.google.gson.annotations.SerializedName

data class ResponseProgramsDto(@SerializedName("result") val result: List<ProgramDto>)
data class ResponseScheduleDto(@SerializedName("result") val result: List<WrappedProgram>)
data class ResponseNowDto(@SerializedName("result") val result: WrappedProgram)

data class WrappedProgram(@SerializedName("program") val program: ProgramDto,
                          @SerializedName("start") val start: String,
                          @SerializedName("end") val end: String,
                          @SerializedName("duration") val duration: String)


data class SessionsDto(@SerializedName("result") val result: List<SessionDto>)

data class SessionDto(@SerializedName("audio") val audio: AudioDto,
                      @SerializedName("appMobileTitle") val title: String,
                      @SerializedName("path") val path: String,
                      @SerializedName("dateTime") val dateTime: String,
                      @SerializedName("durationSeconds") val durationSeconds: Long)

data class AudioDto(@SerializedName("id") val id: String,
                    @SerializedName("description") val description: String,
                    @SerializedName("publicationDate") val publicationDate: String,
                    @SerializedName("date") val date: String,
                    @SerializedName("time") val time: String,
                    @SerializedName("length") val length: Long)

/*
"safeTitle": "2018-04-03-la-competencia-12h",
"path": "https://audioserver.rac1.cat/get/b14a9532-51fa-4994-b0b3-051a37306a17/1/2018-04-03-la-competencia-12h.mp3?source=APP",
"dateTime": "2018-04-03T12:00:00+02:00",
"durationSeconds": 3599
 */