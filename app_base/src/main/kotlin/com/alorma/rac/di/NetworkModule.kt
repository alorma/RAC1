package com.alorma.rac.di

import com.alorma.rac.core.AppAudioTrackProvider
import com.alorma.rac.data.api.ProgramsApiMapper
import com.alorma.rac.data.api.RadioApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    factory { (track: String) ->
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.audioteca.${track}.cat/api/app/v1/")
            .build()
    }

    factory {
        get<Retrofit> {
            val trackProvider = get<AppAudioTrackProvider>()
            val track = trackProvider.audioTrack()
            parametersOf(track.name)
        }.create(RadioApi::class.java)
    }

    factory { ProgramsApiMapper() }
}