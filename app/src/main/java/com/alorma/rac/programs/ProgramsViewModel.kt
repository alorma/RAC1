package com.alorma.rac.programs

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.alorma.rac.core.BaseViewModel
import com.alorma.rac.data.api.Program
import com.alorma.rac.data.api.Rac1Api

class ProgramsViewModel(private val rac1Api: Rac1Api) : BaseViewModel() {

    val programs: LiveData<List<Program>> = liveData {
        rac1Api.programs().body()?.result?.let {
            emit(it)
        }
    }
}