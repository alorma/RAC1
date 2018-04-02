package com.alorma.rac1

import android.app.Application
import com.alorma.rac1.di.ApplicationComponent
import com.alorma.rac1.di.DaggerApplicationComponent
import com.alorma.rac1.di.module.ApplicationModule
import com.jakewharton.threetenabp.AndroidThreeTen

class Rac1Application : Application() {

    companion object {
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}
