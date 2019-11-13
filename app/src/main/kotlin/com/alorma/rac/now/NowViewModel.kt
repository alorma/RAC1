package com.alorma.rac.now

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.alorma.rac.core.BaseViewModel
import com.alorma.rac.data.api.Program
import com.alorma.rac.data.api.RacAudioApi

class NowViewModel(private val racAudioApi: RacAudioApi) : BaseViewModel() {

    val now: LiveData<Program> = liveData {
        racAudioApi.now().body()?.result?.program?.let {
            val schedule = it.schedule
                .split(",")
                .map { part -> part.trim() }
                .last { part -> part.isNotEmpty() }

            val program = it.copy(schedule = schedule)
            emit(program)
        }
    }

}