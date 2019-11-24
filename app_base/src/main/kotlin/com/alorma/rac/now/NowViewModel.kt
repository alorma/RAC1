package com.alorma.rac.now

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.alorma.rac.core.BaseViewModel
import com.alorma.rac.data.api.ProgramApiModel
import com.alorma.rac.data.api.RadioApi

class NowViewModel(private val radioApi: RadioApi) : BaseViewModel() {

    val now: LiveData<ProgramApiModel> = liveData {
        radioApi.now().body()?.result?.programApiModel?.let {
            val schedule = it.schedule
                ?.split(",")
                ?.map { part -> part.trim() }
                ?.last { part -> part.isNotEmpty() }

            val program = it.copy(schedule = schedule)
            emit(program)
        }
    }

}