package com.alorma.rac1.di;

import com.alorma.rac1.di.module.ApplicationModule
import com.alorma.rac1.di.module.NetworkModule
import com.alorma.rac1.service.LiveRadioService
import com.alorma.rac1.ui.MainActivity
import com.alorma.rac1.ui.ScheduleFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class])
interface ApplicationComponent {
    infix fun inject(activity: MainActivity)

    infix fun inject(scheduleFragment: ScheduleFragment)

    infix fun inject(liveRadioService: LiveRadioService)
}