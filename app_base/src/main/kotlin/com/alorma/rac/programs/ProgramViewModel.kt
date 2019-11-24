package com.alorma.rac.programs

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.alorma.rac.core.BaseViewModel
import com.alorma.rac.data.api.ProgramApiModel
import com.alorma.rac.data.api.RadioApi

class ProgramViewModel(
    programId: String,
    private val radioApi: RadioApi
) : BaseViewModel() {

    val programApiModel: LiveData<ProgramApiModel> = liveData {
        radioApi.programs().result
            ?.firstOrNull { it.id == programId }
            ?.let { emit(it) }
    }
}