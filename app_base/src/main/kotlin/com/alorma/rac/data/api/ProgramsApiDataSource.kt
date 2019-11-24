package com.alorma.rac.data.api

import com.alorma.rac.domain.model.Program

class ProgramsApiDataSource(
    private val api: RadioApi,
    private val mapper: ProgramsApiMapper
) {
    suspend fun getPrograms(): List<Program> {
        return api.programs().result?.let {
            mapper.map(it)
        } ?: emptyList()
    }

    suspend fun getNow(): Program? {
        return api.now().result?.programApiModel?.let {
            mapper.map(it)
        }
    }
}