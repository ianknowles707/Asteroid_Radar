package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.ImageApi
import com.udacity.asteroidradar.data.AsteroidRepository
import com.udacity.asteroidradar.data.getDatabase
import kotlinx.coroutines.launch
import retrofit2.HttpException

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    //Define variable to show Asteroid data
    val showAsteroidList: LiveData<List<Asteroid>>

    //Define encapsulated variable to handle navigation to the Detail Fragment
    private val _showSelectedAsteroid = MutableLiveData<Asteroid>()

    val showSelectedAsteroid: LiveData<Asteroid>
        get() = _showSelectedAsteroid

    //Define encapsulated variable for image of the day
    private val _dailyImage = MutableLiveData<PictureOfDay>()

    val dailyImage: LiveData<PictureOfDay>
        get() = _dailyImage

    //Get an instance of the database
    private val database = getDatabase(application)

    //get an instance of the repository
    private val asteroidRepository = AsteroidRepository(database)

    init {

        //Call repository function to refresh database with new data
        viewModelScope.launch {
            _dailyImage.value = getImageOfTheDay()
            asteroidRepository.updateAsteroids()
        }

        //Get data to show in app from stored value in database
        showAsteroidList = asteroidRepository.cachedAsteroids

    }

    //Set the navigation handler to take the value of the selected Asteroid
    fun displaySelectedAsteroidDetails(asteroid: Asteroid) {
        _showSelectedAsteroid.value = asteroid
    }

    //Reset the navigation handler to null to signify navigation complete
    fun doneDisplayingAsteroidDetails() {
        _showSelectedAsteroid.value = null
    }

     suspend fun getImageOfTheDay(): PictureOfDay? {
        var dailyImage: PictureOfDay? = null
        try {
            dailyImage = ImageApi.retrofitService.getImage(Constants.API_KEY)
        } catch (e: HttpException) {
            //error handling
        }
        return dailyImage
    }
}