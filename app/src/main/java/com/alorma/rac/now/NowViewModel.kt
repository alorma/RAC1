package com.alorma.rac.now

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.alorma.rac.core.BaseViewModel
import com.alorma.rac.data.api.Program
import com.alorma.rac.data.api.Rac1Api

class NowViewModel(private val rac1Api: Rac1Api) : BaseViewModel() {

    val now: LiveData<Program> = liveData {
        rac1Api.now().body()?.result?.program?.let {
            val program = it.copy(schedule = it.schedule.split(",").last())
            emit(program)
        }
    }

}