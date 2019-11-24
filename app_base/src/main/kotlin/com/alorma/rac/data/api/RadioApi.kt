package com.alorma.rac.data.api

import retrofit2.http.GET

interface RadioApi {

    @GET("now")
    suspend fun now(): NowResponse

    @GET("programs")
    suspend fun programs(): ProgramsResponse
}