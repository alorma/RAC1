package com.alorma.rac1.di

import com.alorma.rac1.di.module.ApplicationModule
import com.alorma.rac1.di.module.DataModule
import com.alorma.rac1.di.module.NetworkModule
import com.alorma.rac1.service.LiveRadioService
import com.alorma.rac1.ui.MainActivity
import com.alorma.rac1.ui.PlayConnectionFragment
import com.alorma.rac1.ui.program.LiveProgramFragment
import com.alorma.rac1.ui.program.ProgramActivity
import com.alorma.rac1.ui.program.ProgramInfoFragment
import com.alorma.rac1.ui.program.ProgramPodcastFragment
import com.alorma.rac1.ui.programs.ProgramsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class, DataModule::class])
interface ApplicationComponent {
    infix fun inject(activity: MainActivity)
    infix fun inject(liveFragment: ProgramActivity)

    infix fun inject(programsFragment: ProgramsFragment)
    infix fun inject(liveFragment: LiveProgramFragment)
    infix fun inject(programInfoFragment: ProgramInfoFragment)
    infix fun inject(programDetailFragment: ProgramPodcastFragment)

    infix fun inject(PlayConnectionFragment: PlayConnectionFragment)

    infix fun inject(liveRadioService: LiveRadioService)
}
