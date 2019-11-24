package com.alorma.rac.domain.model

import com.alorma.rac.data.ProgramsRepository
import org.koin.dsl.module

val domainModule = module {
    factory { ProgramsRepository(get(), get()) }
}