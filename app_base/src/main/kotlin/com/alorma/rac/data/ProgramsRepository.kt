package com.alorma.rac.data

import com.alorma.rac.data.api.ProgramsApiDataSource
import com.alorma.rac.data.db.ProgramsDbDataSource
import com.alorma.rac.domain.model.Program
import kotlinx.coroutines.flow.Flow

class ProgramsRepository(
    private val dbDataSource: ProgramsDbDataSource,
    private val apiDataSource: ProgramsApiDataSource
) {
    suspend fun getPrograms(): Flow<List<Program>> = dbDataSource.getPrograms().also {
        val programs = apiDataSource.getPrograms()
        val now = apiDataSource.getNow()
        val savePrograms = now?.let {
            programs.map { it.copy(now = it.id == now.id) }
        } ?: programs
        dbDataSource.savePrograms(savePrograms)
    }

    suspend fun getNow(): Flow<Program?> {
        return dbDataSource.getNow()
    }

}