package com.alorma.rac.now

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.alorma.rac.core.BaseViewModel
import com.alorma.rac.data.ProgramsRepository
import com.alorma.rac.domain.model.Program
import kotlinx.coroutines.flow.collect

class NowViewModel(
    private val repository: ProgramsRepository
) : BaseViewModel() {

    val now: LiveData<Program> = liveData {
        repository.getNow().collect {
            it?.let {
                emit(it)
            }
        }
    }

}