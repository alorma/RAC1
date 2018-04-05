package com.alorma.rac1.di

import com.alorma.rac1.di.module.ApplicationModule
import com.alorma.rac1.di.module.DataModule
import com.alorma.rac1.di.module.NetworkModule
import com.alorma.rac1.service.LiveRadioService
import com.alorma.rac1.ui.MainActivity
import com.alorma.rac1.ui.PlayConnectionFragment
import com.alorma.rac1.ui.now.NowPlayingFragment
import com.alorma.rac1.ui.program.ProgramFragment
import com.alorma.rac1.ui.program.ProgramInfoFragment
import com.alorma.rac1.ui.program.ProgramProdcastFragment
import com.alorma.rac1.ui.programs.ProgramsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class, DataModule::class])
interface ApplicationComponent {
    infix fun inject(activity: MainActivity)

    infix fun inject(programsFragment: ProgramsFragment)
    infix fun inject(liveFragment: ProgramFragment)
    infix fun inject(programInfoFragment: ProgramInfoFragment)
    infix fun inject(programDetailFragment: ProgramProdcastFragment)

    infix fun inject(PlayConnectionFragment: PlayConnectionFragment)
    infix fun inject(nowPlayingFragment: NowPlayingFragment)

    infix fun inject(liveRadioService: LiveRadioService)
}
