package com.alorma.rac.programs

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.alorma.rac.core.BaseViewModel
import com.alorma.rac.data.api.Program
import com.alorma.rac.data.api.RacAudioApi

class ProgramsViewModel(private val racAudioApi: RacAudioApi) : BaseViewModel() {

    val programs: LiveData<List<Program>> = liveData {
        racAudioApi.programs().body()?.result?.let {
            emit(it)
        }
    }
}