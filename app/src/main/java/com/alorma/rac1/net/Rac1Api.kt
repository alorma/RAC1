package com.alorma.rac1.net

import io.reactivex.Completable
import retrofit2.http.GET

interface Rac1Api {

    @GET("properties")
    fun properties(): Completable

    @GET("now")
    fun now(): Completable

    @GET("schedule")
    fun schedule(): Completable

    @GET("programs")
    fun programs(): Completable
}