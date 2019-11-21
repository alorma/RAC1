package com.alorma.rac.tv.base.di

import com.alorma.rac.tv.base.IntentFactory
import com.alorma.rac.tv.program.ProgramIntentFactory
import com.alorma.rac.tv.programs.ProgramsIntentFactory
import org.koin.dsl.module

val navigationModule = module {
    single { ProgramsIntentFactory() }
    single { ProgramIntentFactory() }
    single { IntentFactory(get(), get()) }
}