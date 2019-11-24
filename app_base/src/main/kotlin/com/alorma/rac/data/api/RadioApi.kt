package com.alorma.rac.data.api

import retrofit2.Response
import retrofit2.http.GET

interface RadioApi {

    @GET("now")
    suspend fun now(): Response<NowResponse>

    @GET("programs")
    suspend fun programs(): Response<ProgramsResponse>
}