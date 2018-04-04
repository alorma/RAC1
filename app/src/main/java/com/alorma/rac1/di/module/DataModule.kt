package com.alorma.rac1.di.module

import com.alorma.rac1.data.TimeProvider
import com.alorma.rac1.data.TimeToLive
import com.alorma.rac1.data.cache.ProgramsCacheHandler
import com.alorma.rac1.service.StreamPlayback
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataModule {

    companion object {
        const val NOW_CACHE = "NOW_CACHE"
        const val PROGRAMS_CACHE = "PROGRAMS_CACHE"
        const val SCHEDULE_CACHE = "SCHEDULE_CACHE"

        const val NOW_CACHE_TIME = "NOW_CACHE_TIME"
        const val PROGRAMS_CACHE_TIME = "PROGRAMS_CACHE_TIME"
        const val SCHEDULE_CACHE_TIME = "SCHEDULE_CACHE_TIME"
    }

    @Provides
    @Named(NOW_CACHE_TIME)
    fun getTimeToLiveNow(timeProvider: TimeProvider): TimeToLive =
            TimeToLive(timeProvider, 10, TimeUnit.MINUTES)

    @Provides
    @Named(SCHEDULE_CACHE_TIME)
    fun getTimeToLiveSchedule(timeProvider: TimeProvider): TimeToLive =
            TimeToLive(timeProvider, 5, TimeUnit.MINUTES)

    @Provides
    @Named(PROGRAMS_CACHE_TIME)
    fun getTimeToLivePrograms(timeProvider: TimeProvider): TimeToLive =
            TimeToLive(timeProvider, 1, TimeUnit.HOURS)

    @Provides
    @Singleton
    @Named(NOW_CACHE)
    fun getNowCache(
            timeProvider: TimeProvider, @Named(NOW_CACHE_TIME) timeToLive: TimeToLive) =
            ProgramsCacheHandler(timeProvider, timeToLive)

    @Provides
    @Singleton
    @Named(SCHEDULE_CACHE)
    fun getScheduleCache(
            timeProvider: TimeProvider, @Named(SCHEDULE_CACHE_TIME) timeToLive: TimeToLive) =
            ProgramsCacheHandler(timeProvider, timeToLive)

    @Provides
    @Singleton
    @Named(PROGRAMS_CACHE)
    fun getProgramsCache(
            timeProvider: TimeProvider, @Named(PROGRAMS_CACHE_TIME) timeToLive: TimeToLive) =
            ProgramsCacheHandler(timeProvider, timeToLive)

    @Provides
    @Singleton
    fun getPlaybackPublisher(): PublishSubject<StreamPlayback> = PublishSubject.create()
}