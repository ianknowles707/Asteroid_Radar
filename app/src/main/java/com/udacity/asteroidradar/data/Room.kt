package com.udacity.asteroidradar.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

//Set variable for the single instance of the database that will be used
private lateinit var INSTANCE: AsteroidDatabase

//Set up the table containing the required elements of the asteroid data
//Primary key will be the asteroid ID (unique)
@Entity(tableName = "asteroid_data")
data class DatabaseAsteroid(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "name")
    val codename: String,
    @ColumnInfo(name = "date")
    val closeApproachDate: String,
    @ColumnInfo(name = "mag")
    val absoluteMagnitude: Double,
    @ColumnInfo(name = "diam")
    val estimatedDiameter: Double,
    @ColumnInfo(name = "vel")
    val relativeVelocity: Double,
    @ColumnInfo(name = "dist")
    val distanceFromEarth: Double,
    @ColumnInfo(name = "hazard")
    val isPotentiallyHazardous: Boolean
)

//Establish the functions required to enter and retrieve data from the database
@Dao
interface AsteroidDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg asteroid: DatabaseAsteroid)

    @Query("SELECT * FROM asteroid_data ORDER BY date ASC")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>
}

//Create the database abstract class, referring to the entities and DAO created above
@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDataDao
}

//Use Room database builder to create the instance of the database (if one is not
// already initialized)
fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids"
            ).build()
        }
    }
    return INSTANCE
}

//Provide mapping from DatabaseAsteroid to Asteroid object
fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

//Provide mapping from Asteroid object to array of DatabaseAsteroids
fun List<Asteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}