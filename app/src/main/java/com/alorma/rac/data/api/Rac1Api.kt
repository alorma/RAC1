package com.alorma.rac.data.api

import retrofit2.Response
import retrofit2.http.GET

interface Rac1Api {

    @GET("now")
    suspend fun now(): Response<NowResponse>
}