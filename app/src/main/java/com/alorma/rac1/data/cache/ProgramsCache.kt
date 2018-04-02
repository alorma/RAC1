package com.alorma.rac1.data.cache

import com.alorma.rac1.data.net.ProgramDto
import com.alorma.rac1.di.module.DataModule
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class ProgramsCache @Inject constructor(
        @Named(DataModule.SCHEDULE_CACHE) private val schedule: ProgramsCacheHandler,
        @Named(DataModule.PROGRAMS_CACHE) private val programs: ProgramsCacheHandler) {

    fun getSchedule(): Single<List<ProgramDto>> = schedule.get()
    fun saveSchedule(it: List<ProgramDto>) {
        schedule.save(it)
    }

    fun getPrograms(): Single<List<ProgramDto>> = programs.get()
    fun savePrograms(it: List<ProgramDto>) {
        programs.save(it)
    }
}