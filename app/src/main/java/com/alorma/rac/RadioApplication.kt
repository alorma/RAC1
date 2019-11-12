package com.alorma.rac

import android.app.Application
import com.alorma.rac.di.networkModule
import com.alorma.rac.di.uiModule
import com.gabrielittner.threetenbp.LazyThreeTen
import com.sergiandreplace.androiddatetimetextprovider.AndroidDateTimeTextProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.threeten.bp.format.DateTimeTextProvider

class RadioApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        LazyThreeTen.init(this)
        DateTimeTextProvider.setInitializer(AndroidDateTimeTextProvider())

        startKoin {
            androidContext(this@RadioApplication)
            modules(listOf(uiModule, networkModule))
        }
    }
}