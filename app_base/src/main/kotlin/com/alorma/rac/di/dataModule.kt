package com.alorma.rac.di

import androidx.work.WorkManager
import com.alorma.rac.data.api.ProgramsApiDataSource
import com.alorma.rac.data.db.ProgramsDbDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { WorkManager.getInstance(androidContext()) }
    factory { ProgramsApiDataSource(get(), get()) }
    factory { ProgramsDbDataSource(get(), get(), get(), get()) }
}