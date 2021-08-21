package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Asteroid

class DetailViewModel(asteroid: Asteroid, app: Application) : AndroidViewModel(app) {

    //Define a variable to receive the selected Asteroid
    private val _selectedAsteroid = MutableLiveData<Asteroid>()

    val selectedAsteroid: LiveData<Asteroid>
        get() = _selectedAsteroid

    init {
        //Set the value of the variable equal to the Asteroid passed into the ViewModel
        _selectedAsteroid.value = asteroid
    }
}