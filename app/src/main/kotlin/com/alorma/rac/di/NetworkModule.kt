package com.alorma.rac.di

import com.alorma.rac.core.AppAudioTrackProvider
import com.alorma.rac.data.api.RacAudioApi
import okhttp3.OkHttpClient
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        OkHttpClient.Builder().build()
    }

    factory { (url: String) ->
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build()
    }

    factory {
        get<Retrofit> {
            val trackProvider = get<AppAudioTrackProvider>()
            val track = trackProvider.audioTrack()
            parametersOf("https://api.audioteca.${track.name}.cat/api/app/v1/")
        }.create(RacAudioApi::class.java)
    }
}