package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.Asteroid
import java.lang.IllegalArgumentException

//The DetailViewModel needs to be created with an Asteroid object. The ViewModelFactory will
//be used to create the custom ViewModel
class DetailViewModelFactory(private val asteroid: Asteroid, private val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(asteroid, app) as T
        } else{
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}