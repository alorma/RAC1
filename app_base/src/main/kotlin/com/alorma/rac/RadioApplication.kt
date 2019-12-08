package com.alorma.rac

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coil.Coil
import coil.ImageLoader
import coil.util.CoilUtils
import com.alorma.rac.di.*
import com.alorma.rac.domain.model.domainModule
import com.alorma.rac.work.UpdateProgramsWork
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import com.gabrielittner.threetenbp.LazyThreeTen
import com.sergiandreplace.androiddatetimetextprovider.AndroidDateTimeTextProvider
import okhttp3.OkHttpClient
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.threeten.bp.format.DateTimeTextProvider
import java.util.concurrent.TimeUnit


abstract class RadioApplication : Application() {

    private val workManager: WorkManager by inject()

    override fun onCreate() {
        super.onCreate()

        initFlipper()
        initDates()
        initImageLoader()

        startKoin {
            androidContext(this@RadioApplication)
            modules(
                listOf(
                    coreModule,
                    networkModule,
                    databaseModule,
                    dataModule,
                    domainModule,
                    uiModule
                ) + koinModules()
            )
        }

        scheduleApiWork()
    }

    private fun initDates() {
        LazyThreeTen.init(this)
        DateTimeTextProvider.setInitializer(AndroidDateTimeTextProvider())
    }

    private fun initImageLoader() {
        Coil.setDefaultImageLoader {
            ImageLoader(this) {
                okHttpClient {
                    OkHttpClient.Builder()
                        .cache(CoilUtils.createDefaultCache(this@RadioApplication))
                        .build()
                }
            }
        }
    }

    private fun initFlipper() {
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            SoLoader.init(this, false)
            AndroidFlipperClient.getInstance(this).apply {
                addPlugin(
                    InspectorFlipperPlugin(
                        this@RadioApplication,
                        DescriptorMapping.withDefaults()
                    )
                )
                addPlugin(DatabasesFlipperPlugin(this@RadioApplication))
                addPlugin(SharedPreferencesFlipperPlugin(this@RadioApplication))
            }.start()
        }
    }

    private fun scheduleApiWork() {
        val periodic = PeriodicWorkRequestBuilder<UpdateProgramsWork>(15L, TimeUnit.MINUTES).build()
        workManager.enqueueUniquePeriodicWork(
            "Periodic sync",
            ExistingPeriodicWorkPolicy.KEEP,
            periodic
        )
    }

    open fun koinModules(): List<Module> = emptyList()
}