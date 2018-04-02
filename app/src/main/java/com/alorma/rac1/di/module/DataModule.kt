package com.alorma.rac1.di.module

import com.alorma.rac1.data.TimeProvider
import com.alorma.rac1.data.TimeToLive
import com.alorma.rac1.data.cache.ProgramsCacheHandler
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataModule {

    companion object {
        const val PROGRAMS_CACHE = "PROGRAMS_CACHE"
        const val SCHEDULE_CACHE = "SCHEDULE_CACHE"
    }

    @Provides
    fun getTimeToLive(timeProvider: TimeProvider): TimeToLive =
            TimeToLive(timeProvider, 15, TimeUnit.SECONDS)

    @Provides
    @Singleton
    @Named(SCHEDULE_CACHE)
    fun getScheduleCache(timeProvider: TimeProvider, timeToLive: TimeToLive) =
            ProgramsCacheHandler(timeProvider, timeToLive)

    @Provides
    @Singleton
    @Named(PROGRAMS_CACHE)
    fun getProgramsCache(timeProvider: TimeProvider, timeToLive: TimeToLive) =
            ProgramsCacheHandler(timeProvider, timeToLive)

}