package com.alorma.rac1.di;

import com.alorma.rac1.di.module.ApplicationModule
import com.alorma.rac1.di.module.NetworkModule
import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class])
interface ApplicationComponent {

}