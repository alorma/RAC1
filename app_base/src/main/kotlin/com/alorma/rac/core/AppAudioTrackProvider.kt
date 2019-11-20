package com.alorma.rac.core

import android.content.SharedPreferences
import com.alorma.rac.extension.edit

class AppAudioTrackProvider(private val sharedPreferences: SharedPreferences) {

    fun audioTrack(): AppAudioTrack {
        return when (sharedPreferences.getString(APP_AUDIO_TRACK, AppAudioTrack.Rac1.name)) {
            AppAudioTrack.Rac105.name -> AppAudioTrack.Rac105
            else -> AppAudioTrack.Rac1
        }
    }

    fun save(appAudioTrack: AppAudioTrack) {
        sharedPreferences.edit {
            putString(APP_AUDIO_TRACK, appAudioTrack.name)
        }
    }

    companion object {
        private const val APP_AUDIO_TRACK = "app_audio_track"
    }

}