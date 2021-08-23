package com.udacity.asteroidradar.data

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidAPI
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {

    //Define function to get today's date in correct format for network request
    fun getToday(): String {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }


    //Update the list of Asteroids from the network Api and parse using provided function
    //Convert to array of DatabaseAAsteroids to add to database
    suspend fun updateAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val networkResponse =
                    AsteroidAPI.retrofitService.getAsteroids(getToday(), Constants.API_KEY)
                val asteroids = parseAsteroidsJsonResult(JSONObject(networkResponse))
                database.asteroidDao.insert(*asteroids.asDatabaseModel())
            } catch (e: Exception) {
                //Error handling
            }

        }
    }

    //Retrieve list of asteroids from the database by running the query, then converting to
    //Asteroid objects
    val cachedAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()){
            it.asDomainModel()
        }


}