package com.alorma.rac.data

import com.alorma.rac.data.api.ProgramsApiDataSource
import com.alorma.rac.data.db.ProgramsDbDataSource
import com.alorma.rac.domain.model.Program
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

@ExperimentalCoroutinesApi
class ProgramsRepository(
    private val dbDataSource: ProgramsDbDataSource,
    private val apiDataSource: ProgramsApiDataSource
) {
    fun getPrograms(): Flow<List<Program>> = dbDataSource.getPrograms()
        .onStart { downloadPrograms() }

    private suspend fun downloadPrograms() {
        val programs = apiDataSource.getPrograms()
        val now = apiDataSource.getNow()
        val savePrograms = now?.let {
            programs.map { it.copy(now = it.id == now.id) }
        } ?: programs
        dbDataSource.savePrograms(savePrograms)
    }

    fun getProgram(programId: String): Flow<Program> = dbDataSource.getProgram(programId)

    fun getNow(): Flow<Program?> = dbDataSource.getNow()

}