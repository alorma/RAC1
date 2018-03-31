package com.alorma.rac1.net

import io.reactivex.Single
import retrofit2.http.GET

interface Rac1Api {

    @GET("now")
    fun now(): Single<ResponseNowDto>

    @GET("schedule")
    fun schedule(): Single<ResponseScheduleDto>

    @GET("programs")
    fun programs(): Single<ResponseProgramsDto>
}