package raum.muchbeer.knowyourcity.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.location.Geofence
import raum.muchbeer.knowyourcity.data.*

class ICityRepositoryImpl(val cityDao: CityDao) : ICityRepository {
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