package com.alorma.rac.listening

sealed class ListeningStatus {

    object Nothing : ListeningStatus()
    sealed class Playing : ListeningStatus() {
        object Now : Playing()
        data class Podcast(val id: String) : Playing()
    }
}