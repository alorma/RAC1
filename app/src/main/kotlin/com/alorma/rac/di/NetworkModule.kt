package com.alorma.rac.di

import com.alorma.rac.data.api.Rac1Api
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        OkHttpClient.Builder().build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.audioteca.rac1.cat/api/app/v1/")
            .build()
    }

    factory { get<Retrofit>().create(Rac1Api::class.java) }
}