package com.alorma.rac1.di.module

import android.content.Context
import com.alorma.rac1.BuildConfig
import com.alorma.rac1.data.net.Rac1Api
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class NetworkModule {

    companion object {
        private const val LOGGING_INTERCEPTOR = "LoggingInterceptor"
        private const val CHUCK_INTERCEPTOR = "ChuckInterceptor"
        private const val API_URL = "https://api.audioteca.rac1.cat/api/app/v1/"
    }

    @Provides
    fun getRac1(retrofit: Retrofit): Rac1Api = retrofit.create(Rac1Api::class.java)

    @Provides
    fun getApiRetrofit(retrofitBuilder: Retrofit.Builder): Retrofit = retrofitBuilder.baseUrl(API_URL).build()

    @Provides
    fun providesRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(Gson()))


    @Provides
    fun getOkHttpClient(@Named(LOGGING_INTERCEPTOR) logInterceptor: Interceptor,
                        @Named(CHUCK_INTERCEPTOR) chuckInterceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .addInterceptor(chuckInterceptor)
            .build()

    @Provides
    @Named(LOGGING_INTERCEPTOR)
    fun getLogInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Named(CHUCK_INTERCEPTOR)
    fun getChuckInterceptor(context: Context): Interceptor = ChuckInterceptor(context)
}