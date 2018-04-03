package com.alorma.rac1.data.net

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface Rac1Api {

    @GET("now")
    fun now(): Single<ResponseNowDto>

    @GET("schedule")
    fun schedule(): Single<ResponseScheduleDto>

    @GET("programs")
    fun programs(): Single<ResponseProgramsDto>

    @GET("sessions/{program}/{section}")
    fun getSessions(@Path("program") program: String,
                    @Path("section") section: String): Single<SessionsDto>
}