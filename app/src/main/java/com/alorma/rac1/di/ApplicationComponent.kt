package com.alorma.rac1.di;

import com.alorma.rac1.di.module.ApplicationModule
import com.alorma.rac1.di.module.NetworkModule
import com.alorma.rac1.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class])
interface ApplicationComponent {
    infix fun inject(activity: MainActivity)
}