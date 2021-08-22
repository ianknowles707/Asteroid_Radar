package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

//Define functions needed for the connection to NASA NeoWS
interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") key: String
    ): String
}

//Create the retrofitservice using the interface
object AsteroidAPI {
    val retrofitService: AsteroidApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(AsteroidApiService::class.java)
    }
}