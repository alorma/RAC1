package com.alorma.rac

import android.app.Application
import com.alorma.rac.di.networkModule
import com.alorma.rac.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RadioApplication : Application() {

    override fun onCreate() {
        super.onCreate()



        startKoin {
            androidContext(this@RadioApplication)
            modules(listOf(uiModule, networkModule))
        }
    }
}