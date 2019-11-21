package com.alorma.rac.programs

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.alorma.rac.core.BaseViewModel
import com.alorma.rac.data.api.Program
import com.alorma.rac.data.api.RacAudioApi

class ProgramViewModel(
    programId: String,
    private val racAudioApi: RacAudioApi
) : BaseViewModel() {

    val program: LiveData<Program> = liveData {
        racAudioApi.programs().body()
            ?.result
            ?.firstOrNull { it.id == programId }
            ?.let { emit(it) }
    }
}