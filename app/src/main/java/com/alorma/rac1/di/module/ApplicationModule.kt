package com.alorma.rac1.di.module

import android.content.Context
import android.content.SharedPreferences
import com.alorma.rac1.service.LocalPlayback
import com.alorma.rac1.service.Playback
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class ApplicationModule(private val context: Context) {

    companion object {
        private const val APP_PREFERENCES: String = "SharedPreferences"
    }

    @Provides
    @Singleton
    fun getGlide(): RequestManager = Glide.with(context)

    @Provides
    @Singleton
    open fun providesSharedPreferences(): SharedPreferences =
            context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    fun getContext(): Context = context

    @Provides
    fun getPlayback(context: Context): Playback = LocalPlayback(context)

}