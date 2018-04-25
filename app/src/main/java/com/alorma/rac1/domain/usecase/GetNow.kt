package com.alorma.rac1.domain.usecase

import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.ProgramsRepository
import io.reactivex.Single
import javax.inject.Inject

class GetNow @Inject constructor(private val programsRepository: ProgramsRepository) {
    fun execute(): Single<ProgramItem> = programsRepository.getNow()
}