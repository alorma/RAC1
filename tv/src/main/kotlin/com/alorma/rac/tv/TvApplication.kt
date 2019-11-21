package com.alorma.rac.tv

import com.alorma.rac.RadioApplication
import com.alorma.rac.tv.base.di.navigationModule
import org.koin.core.module.Module

class TvApplication : RadioApplication() {
    override fun koinModules(): List<Module> = listOf(navigationModule)
}