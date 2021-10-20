package raum.muchbeer.knowyourcity.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(locations: List<Location>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(activities: List<Activity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityLocationCrossRefs(activityLocationCrossRefs: List<ActivityLocationCrossRef>)

    @Query("SELECT * FROM Activity ORDER BY title")
    fun getAllActivities(): LiveData<List<Activity>>

    @Query("SELECT * FROM Location")
    fun getAllLocations(): LiveData<List<Location>>

    @Transaction
    @Query("SELECT * FROM Activity WHERE activityId = :activityId")
    fun getActivityWithLocations(activityId: Int): LiveData<ActivityWithLocations>

    @Query("SELECT * FROM Location WHERE locationId = :locationId")
    suspend fun getLocationById(locationId: Int): Location

    @Transaction
    @Query("SELECT DISTINCT L.* FROM Location L, Activity A, ActivityLocationCrossRef AL WHERE L.locationId = AL.locationId AND AL.activityId = A.activityId AND A.geofenceEnabled != 0")
    suspend fun getLocationsForGeofencing(): List<Location>

    @Transaction
    @Query("SELECT * FROM Location WHERE locationId = :locationId")
    fun getLocationWithActivities(locationId: Int): LiveData<LocationWithActivities>

    @Query("UPDATE activity set geofenceEnabled = ~geofenceEnabled WHERE activityId = :id")
    suspend fun toggleGeofenceEnabled(id: Int): Int
}