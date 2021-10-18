package raum.muchbeer.knowyourcity.repository

import androidx.lifecycle.LiveData
import raum.muchbeer.knowyourcity.data.*

interface ICityRepository {
    fun getAllActivities(): LiveData<List<Activity>>

    fun getAllLocations(): LiveData<List<Location>>

    fun getActivityWithLocations(activityId: Int): LiveData<ActivityWithLocations>

    suspend fun getLocationById(locationId: Int): Location

    fun getLocationWithActivities(locationId: Int): LiveData<LocationWithActivities>

    suspend fun toggleActivityGeofence(id: Int): GeofencingChanges
}