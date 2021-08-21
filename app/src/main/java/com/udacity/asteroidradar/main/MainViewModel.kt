package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {

    //Define encapsulated variable to hold the list of Asteroids retrieved from the network
    private val _asteroids = MutableLiveData<List<Asteroid>>()

    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    //Define encapsulated variable to handle navigation to the Detail Fragment
    private val _showSelectedAsteroid = MutableLiveData<Asteroid>()

    val showSelectedAsteroid: LiveData<Asteroid>
        get() = _showSelectedAsteroid


    init {
        //Temporary data for testing the RecyclerView and arguments to Detail Fragment.
        //TO REMOVE WHEN FINISHED
        val asteroid_1 = Asteroid(
            12345,
            "Alpha",
            "2021-Sep-01",
            16.12,
            745.1,
            8.34,
            0.9,
            false
        )

        val asteroid_2 = Asteroid(
            12345,
            "Beta",
            "2021-Sep-02",
            12.12,
            885.1,
            44.34,
            0.001,
            true
        )

        val asteroid_3 = Asteroid(
            12345,
            "Gamma",
            "2021-Sep-03",
            10.12,
            745.1,
            17.34,
            0.3,
            false
        )

        val asteroid_4 = Asteroid(
            12345,
            "Delta",
            "2021-Sep-04",
            5.12,
            1745.1,
            47.34,
            0.01,
            false
        )

        val asteroid_5 = Asteroid(
            12345,
            "Epsilon",
            "2021-Sep-05",
            14.12,
            75.1,
            1.34,
            0.1,
            true
        )

        _asteroids.value = mutableListOf(
            asteroid_1,
            asteroid_2,
            asteroid_3,
            asteroid_4,
            asteroid_5
        )
    }
    //End of init block

    //Set the navigation handler to take the value of the selected Asteroid
    fun displaySelectedAsteroidDetails(asteroid: Asteroid) {
        _showSelectedAsteroid.value = asteroid
        Log.i("Check", "Nav signalled")
    }

    //Reset the navigation handler to null to signify navigation complete
    fun doneDisplayingAsteroidDetails() {
        _showSelectedAsteroid.value = null
    }

}