package com.udacity.asteroidradar.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import retrofit2.HttpException

//Define the worker that will perform the updateAsteroids function according to the
//parameters passed
class DataWorker(appContext: Context, parameters: WorkerParameters) :
    CoroutineWorker(appContext, parameters) {
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.updateAsteroids()
            Result.success()
        } catch (e: HttpException) {
            //In event of failure, set response to be to re-try
            Result.retry()
        }
    }

    //Name the worker for later reference
    companion object {
        const val WORK_NAME = "DataWorker"
    }
}