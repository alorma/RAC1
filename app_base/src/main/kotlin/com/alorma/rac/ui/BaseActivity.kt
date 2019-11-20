package com.alorma.rac.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alorma.rac.core.AppThemeSwitcher
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

abstract class BaseActivity : AppCompatActivity() {

    private val appThemeSwitcher: AppThemeSwitcher by inject {
        parametersOf(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        appThemeSwitcher.init()
        super.onCreate(savedInstanceState)
    }
}