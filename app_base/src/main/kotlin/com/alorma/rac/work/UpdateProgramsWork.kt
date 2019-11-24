package com.alorma.rac.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.KoinComponent

class UpdateProgramsWork(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    override suspend fun doWork(): Result {

        return Result.success()
    }
}