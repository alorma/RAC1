package com.alorma.rac1

import android.app.Application
import com.alorma.rac1.di.ApplicationComponent
import com.alorma.rac1.di.DaggerApplicationComponent
import com.alorma.rac1.di.module.ApplicationModule

class Rac1Application : Application() {

    companion object {
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}
