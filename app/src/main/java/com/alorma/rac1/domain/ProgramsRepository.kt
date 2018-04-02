package com.alorma.rac1.domain

import com.alorma.rac1.data.cache.ProgramsCache
import com.alorma.rac1.data.net.ProgramDto
import com.alorma.rac1.data.net.Rac1Api
import io.reactivex.Single
import javax.inject.Inject

class ProgramsRepository @Inject constructor(
        private val api: Rac1Api,
        private val cache: ProgramsCache) {

    fun getNow(): Single<ProgramDto> = api.now().map { it.result }.map { it.program }

    fun getSchedule(): Single<List<ProgramDto>> =
            cache.getSchedule()
                    .onErrorResumeNext {
                        api.schedule()
                                .map { it.result.map { it.program } }
                                .doOnSuccess { cache.saveSchedule(it) }
                    }

    fun getPrograms(): Single<List<ProgramDto>> =
            cache.getPrograms()
                    .onErrorResumeNext {
                        api.programs()
                                .map { it.result }
                                .doOnSuccess { cache.savePrograms(it) }
                    }

}