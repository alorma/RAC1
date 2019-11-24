package com.alorma.rac.di

import androidx.work.WorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { WorkManager.getInstance(androidContext()) }
}