package com.alorma.rac

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.util.CoilUtils
import com.alorma.rac.di.coreModule
import com.alorma.rac.di.networkModule
import com.alorma.rac.di.uiModule
import com.gabrielittner.threetenbp.LazyThreeTen
import com.sergiandreplace.androiddatetimetextprovider.AndroidDateTimeTextProvider
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.threeten.bp.format.DateTimeTextProvider

abstract class RadioApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        LazyThreeTen.init(this)
        DateTimeTextProvider.setInitializer(AndroidDateTimeTextProvider())

        Coil.setDefaultImageLoader {
            ImageLoader(this) {
                okHttpClient {
                    OkHttpClient.Builder()
                        .cache(CoilUtils.createDefaultCache(this@RadioApplication))
                        .build()
                }
            }
        }

        startKoin {
            androidContext(this@RadioApplication)
            modules(
                listOf(
                    coreModule,
                    networkModule,
                    uiModule
                ) + koinModules()
            )
        }
    }

    open fun koinModules(): List<Module> = emptyList()
}