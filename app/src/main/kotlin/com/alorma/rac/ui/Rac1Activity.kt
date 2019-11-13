package com.alorma.rac.ui

import android.os.Bundle
import com.alorma.rac.core.AppAudioTrack
import com.alorma.rac.core.AppAudioTrackProvider
import org.koin.android.ext.android.inject

class Rac1Activity: MainActivity() {

    private val appAudioTrackProvider: AppAudioTrackProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        appAudioTrackProvider.save(AppAudioTrack.Rac1)
        super.onCreate(savedInstanceState)
    }
}