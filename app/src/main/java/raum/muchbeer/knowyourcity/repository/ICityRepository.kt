package raum.muchbeer.knowyourcity.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import raum.muchbeer.knowyourcity.data.*

interface ICityRepository {
    fun getAllActivities(): LiveData<List<Activity>>

    fun getAllLocations(): LiveData<List<Location>>

    fun getActivityWithLocations(activityId: Int): LiveData<ActivityWithLocations>

    suspend fun getLocationById(locationId: Int): Location

    fun getLocationWithActivities(locationId: Int): LiveData<LocationWithActivities>

    suspend fun toggleActivityGeofence(id: Int): GeofencingChanges

    suspend fun insertWorkout(workout: Workout): Int

    suspend fun insertWorkoutPoint(workoutPoint: WorkoutPoint)

    suspend fun updateWorkout(workout: Workout)

    fun getAllWorkouts(): LiveData<List<Workout>>

    suspend fun getAllRegionsWithPoints(): List<RegionWithPoints>

    fun getAllRegions() : LiveData<List<Region>>

    fun getAllRegionsWthPointsLiveData() : LiveData<List<RegionWithPoints>>

}