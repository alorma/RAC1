package com.alorma.rac.core

import androidx.appcompat.app.AppCompatActivity
import com.alorma.rac.R

class AppThemeSwitcher(
    private val activity: AppCompatActivity,
    private val appAudioTrackProvider: AppAudioTrackProvider
) {

    fun init() {
        val theme = when (appAudioTrackProvider.audioTrack()) {
            AppAudioTrack.Rac1 -> R.style.Rac1Theme
            AppAudioTrack.Rac105 -> R.style.Rac105Theme
        }
        activity.setTheme(theme)
    }

}