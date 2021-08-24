package com.udacity.asteroidradar.data

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
    private fun getToday(): String {
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

    //Retrieve full list of asteroids from the database by running the query, then converting to
    //Asteroid objects
    val cachedAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroids()) {
            it.asDomainModel()
        }

    //Retrieve only asteroids with closest approach of today
    val onlyTodayAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getTodayAsteroids(getToday())) {
            it.asDomainModel()
        }

    //Retrieve one week of data
    val oneWeeksAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids(getToday())) {
            it.asDomainModel()
        }


    //Test function to check database delete works - if OK remove this function and
    //add to scheduled worker task instead
    fun deleteDataBeforeToday() {
        database.asteroidDao.clearData(getToday())
    }

}