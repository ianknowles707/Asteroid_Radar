package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidAPI
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.AsteroidRepository
import com.udacity.asteroidradar.data.getDatabase
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    //Define encapsulated variable to hold the list of Asteroids retrieved from the network
    private val _asteroids = MutableLiveData<List<Asteroid>>()

    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    //Define encapsulated variable to handle navigation to the Detail Fragment
    private val _showSelectedAsteroid = MutableLiveData<Asteroid>()

    val showSelectedAsteroid: LiveData<Asteroid>
        get() = _showSelectedAsteroid

    //Get an instance of the database
    private val database = getDatabase(application)

    //get an instance of the repository
    private val asteroidRepository = AsteroidRepository(database)

    init {

        getAsteroids(getToday())
        viewModelScope.launch {
            asteroidRepository.updateAsteroids()
        }

    }


    //Set the navigation handler to take the value of the selected Asteroid
    fun displaySelectedAsteroidDetails(asteroid: Asteroid) {
        _showSelectedAsteroid.value = asteroid
    }

    //Reset the navigation handler to null to signify navigation complete
    fun doneDisplayingAsteroidDetails() {
        _showSelectedAsteroid.value = null
    }

    //Define function to get today's date in correct format for network request
    fun getToday(): String {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    //Function calls the Asteroid API retrofit service which returns the result as a String.
    //Result is then converted to JSONObject and passed to the parsing function to generate
    //the list of Asteroids
    private fun getAsteroids(today: String) {
        viewModelScope.launch {
            try {
                val returnResult = AsteroidAPI.retrofitService.getAsteroids(
                    today,
                    Constants.API_KEY
                )
                _asteroids.value = parseAsteroidsJsonResult(JSONObject(returnResult))
            } catch (e: Exception) {
                //Handle error here
            }
        }
    }

}