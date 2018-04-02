package com.alorma.rac1.domain

import com.alorma.rac1.net.ProgramDto
import com.alorma.rac1.net.Rac1Api
import io.reactivex.Single
import javax.inject.Inject

class ProgramsRepository @Inject constructor(private val rac1Api: Rac1Api) {
    fun getNow(): Single<ProgramDto> = rac1Api.now().map { it.result }.map { it.program }

    fun getSchedule(): Single<List<ProgramDto>> = rac1Api.schedule().map { it.result.map { it.program } }

    fun getPrograms(): Single<List<ProgramDto>> = rac1Api.programs().map { it.result }
}