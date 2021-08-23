package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.data.DataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

//Extending base application class to add the worker call for periodic update of data
class AsteroidRadarApplication : Application() {

    //Define a coroutine scope for the worker task to run
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        scheduleUpdate()
    }

    private fun scheduleUpdate() {
        applicationScope.launch {
            setupScheduledUpdate()
        }
    }

    private fun setupScheduledUpdate() {
        //Set constraints to be device charging and on WiFi
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            //To avoid multiple errors, the min_sdk value has been updated to 24 in the
            //build.gradle file. Because of this the following will always work. The spec
            //did not call for device idle but this seems sensible for user experience
            .setRequiresDeviceIdle(true)
            .build()

        //Make it a repeating action with daily interval
        val repeatingRequest = PeriodicWorkRequestBuilder<DataWorker>(
            1,
            TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .build()

        //Call the work manager and pass in the repeatingRequest parameter, which already
        //contains the required constraints
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            DataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }


}