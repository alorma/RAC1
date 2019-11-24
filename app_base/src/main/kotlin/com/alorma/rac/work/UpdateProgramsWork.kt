package com.alorma.rac.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alorma.rac.data.api.ProgramsApiMapper
import com.alorma.rac.data.api.RadioApi
import com.alorma.rac.data.db.ProgramsDao
import com.alorma.rac.data.db.ProgramsDbMapper
import org.koin.core.KoinComponent
import org.koin.core.inject

class UpdateProgramsWork(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val radioApi: RadioApi by inject()
    private val programsApiMapper: ProgramsApiMapper by inject()

    private val programsDao: ProgramsDao by inject()
    private val dbMapper: ProgramsDbMapper by inject()

    override suspend fun doWork(): Result {
        val programs = radioApi.programs().body()?.result?.let {
            programsApiMapper.map(it)
        }

        programs?.let {
            dbMapper.mapToEntity(it)
        }?.also {
            programsDao.savePrograms(it)
        }

        return Result.success()
    }
}