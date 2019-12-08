package com.alorma.rac.programs

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.alorma.rac.core.BaseViewModel
import com.alorma.rac.data.ProgramsRepository
import com.alorma.rac.domain.model.Program
import kotlinx.coroutines.flow.collect

class ProgramsViewModel(
    private val repository: ProgramsRepository
) : BaseViewModel() {

    val programs: LiveData<List<Program>> = liveData {
        repository.getPrograms().collect {
            emit(it)
        }
    }

    val now: LiveData<Program> = liveData {
        repository.getNow().collect {
            it?.let { program -> emit(program) }
        }
    }
}