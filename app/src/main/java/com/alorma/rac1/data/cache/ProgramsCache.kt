package com.alorma.rac1.data.cache

import com.alorma.rac1.di.module.DataModule
import com.alorma.rac1.domain.ProgramItem
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class ProgramsCache @Inject constructor(
        @Named(DataModule.NOW_CACHE) private val now: ProgramsCacheHandler,
        @Named(DataModule.SCHEDULE_CACHE) private val schedule: ProgramsCacheHandler,
        @Named(DataModule.PROGRAMS_CACHE) private val programs: ProgramsCacheHandler) {

    fun getNow(): Single<ProgramItem> = now.get().map { it.first() }
    fun saveNow(it: ProgramItem) {
        schedule.save(listOf(it))
    }

    fun getSchedule(): Single<List<ProgramItem>> = schedule.get()
    fun saveSchedule(it: List<ProgramItem>) {
        schedule.save(it)
    }

    fun getPrograms(): Single<List<ProgramItem>> = programs.get()
    fun savePrograms(it: List<ProgramItem>) {
        programs.save(it)
    }
}