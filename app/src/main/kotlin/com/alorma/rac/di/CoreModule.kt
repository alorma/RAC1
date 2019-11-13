package com.alorma.rac.di

import androidx.preference.PreferenceManager
import com.alorma.rac.core.AppAudioTrackProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    factory { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
    factory { AppAudioTrackProvider(get()) }
}