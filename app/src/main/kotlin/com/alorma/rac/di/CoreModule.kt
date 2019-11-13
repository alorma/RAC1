package com.alorma.rac.di

import com.alorma.rac.core.AppAudioTrack
import org.koin.dsl.module

val coreModule = module {
    factory<AppAudioTrack> { AppAudioTrack.Rac105 }
}