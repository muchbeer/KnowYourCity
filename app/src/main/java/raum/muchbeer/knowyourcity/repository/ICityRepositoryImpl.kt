package raum.muchbeer.knowyourcity.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.Geofence
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import raum.muchbeer.knowyourcity.data.*
import raum.muchbeer.knowyourcity.presentation.fragment.LocationsFragment.Companion.BROADCASTrECEIVER

class ICityRepositoryImpl(val cityDao: CityDao) : ICityRepository {

    private val TAG = ICityRepositoryImpl::class.simpleName
    override fun getAllActivities(): LiveData<List<Activity>> {
      return  cityDao.getAllActivities()     }

    override fun getAllLocations(): LiveData<List<Location>> {
      return cityDao.getAllLocations()     }

    override fun getActivityWithLocations(activityId: Int): LiveData<ActivityWithLocations> {
        return cityDao.getActivityWithLocations(activityId)     }

    override suspend fun getLocationById(locationId: Int): Location {
        return cityDao.getLocationById(locationId)     }

    override fun getLocationWithActivities(locationId: Int): LiveData<LocationWithActivities> {
        return cityDao.getLocationWithActivities(locationId)     }

    override suspend fun toggleActivityGeofence(id: Int): GeofencingChanges {
    return toggleGeofence(id) }

    override suspend fun insertWorkout(workout: Workout): Int {
        return cityDao.insertWorkout(workout).toInt()
    }

    override suspend fun insertWorkoutPoint(workoutPoint: WorkoutPoint) {
        cityDao.insertWorkoutPoint(workoutPoint)
    }

    override suspend fun updateWorkout(workout: Workout) {
        cityDao.updateWorkout(workout)
    }

    override fun getAllWorkouts(): LiveData<List<Workout>> {
        return cityDao.getAllWorkouts()
    }

    override suspend fun getAllRegionsWithPoints(): List<RegionWithPoints> {
        return cityDao.getAllRegionsWithPoints()
    }



    private suspend fun toggleGeofence(ids : Int) : GeofencingChanges {
        val previousLocations = cityDao.getLocationsForGeofencing()
        require(cityDao.toggleGeofenceEnabled(ids) == 1) { "Activity not found" }

        val newLocations = cityDao.getLocationsForGeofencing()

        val removedLocations = previousLocations.subtract(newLocations)
        val addedLocations = newLocations.subtract(previousLocations)

        return GeofencingChanges(
            removedLocations.map { l -> l.locationId.toString() },
            addedLocations.map { l -> createGeofence(l) }
        )
    }

    private fun createGeofence(location: Location): Geofence {
        return Geofence.Builder()
            .setRequestId(location.locationId.toString())
            .setCircularRegion(
                location.latitude,
                location.longitude,
                location.geofenceRadius
            )
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
    }
}