package com.alorma.rac.core

sealed class AppAudioTrack(val name: String) {
    object Rac1 : AppAudioTrack("rac1")
    object Rac105 : AppAudioTrack("rac105")
}