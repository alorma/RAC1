package com.alorma.rac1.domain

import com.alorma.rac1.data.cache.ProgramsCache
import com.alorma.rac1.data.net.ProgramDto
import com.alorma.rac1.data.net.Rac1Api
import io.reactivex.Single
import javax.inject.Inject

class ProgramsRepository @Inject constructor(
        private val api: Rac1Api,
        private val cache: ProgramsCache,
        private val programMapper: ProgramMapper) {

    fun getNow(): Single<ProgramDto> = api.now().map { it.result }.map { it.program }

    fun getSchedule(): Single<List<ProgramItem>> =
            cache.getSchedule()
                    .onErrorResumeNext {
                        api.schedule()
                                .map { schedule ->
                                    schedule.result.map {
                                        programMapper.map(it)
                                    }
                                }
                                .doOnSuccess { cache.saveSchedule(it) }
                    }

    fun getPrograms(): Single<List<ProgramItem>> =
            cache.getPrograms()
                    .onErrorResumeNext {
                        api.programs()
                                .map {
                                    it.result.map { programMapper.map(it) }
                                }
                                .doOnSuccess { cache.savePrograms(it) }
                    }

    fun getProgram(id: String): Single<ProgramItem> = getPrograms().flatMap {
        it.firstOrNull { it.id == id }?.let {
            Single.just(it)
        } ?: Single.error(Exception())
    }
}