package com.alorma.rac.di

import androidx.room.Room
import com.alorma.rac.data.db.ProgramsDbMapper
import com.alorma.rac.data.db.RadioDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            RadioDatabase::class.java, "radio-databaser"
        ).build()
    }

    single { get<RadioDatabase>().programsDao() }
    single { get<RadioDatabase>().sectionsDao() }

    factory { ProgramsDbMapper() }
}