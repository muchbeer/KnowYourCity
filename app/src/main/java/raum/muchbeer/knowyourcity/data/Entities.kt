package raum.muchbeer.knowyourcity.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Region(
    @PrimaryKey val id: Int,
    val title: String,
    val color: String
)

@Entity
data class RegionPoint(
    @PrimaryKey(autoGenerate = true) val regionPointId: Int,
    val latitude: Double,
    val longitude: Double,
    val regionId: Int
)

data class RegionWithPoints(
    @Embedded val region: Region,
    @Relation(
        parentColumn = "id",
        entityColumn = "regionId"
    )
    val points: List<RegionPoint>
)

@Entity
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    var duration: Long = 0,
    var distance: Double = 0.0
)

@Entity
data class WorkoutPoint(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workoutId: Int,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double
)

data class WorkoutWithPoints(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val points: List<WorkoutPoint>
)

internal class DatabaseContents(
    val activities: List<Activity>,
    val crossrefs: List<ActivityLocationCrossRef>,
    val locations: List<Location>,
    val regions: List<Region>,
    val regionpoints: List<RegionPoint>
)

enum class EventSubject {
    LocationUpdate
}