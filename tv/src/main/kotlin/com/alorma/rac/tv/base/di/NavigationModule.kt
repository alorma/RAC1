package com.alorma.rac.tv.base.di

import com.alorma.rac.tv.base.IntentFactory
import com.alorma.rac.tv.program.ProgramIntentFactory
import org.koin.dsl.module

val navigationModule = module {
    single { ProgramIntentFactory() }
    single { IntentFactory(get()) }
}