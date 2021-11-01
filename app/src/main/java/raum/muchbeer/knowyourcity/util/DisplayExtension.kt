package raum.muchbeer.knowyourcity.util

import android.location.Location


private const val MILE = 1609f

fun Location.distanceToInMiles(location: Location) = this.distanceTo(location) / MILE

fun Double.toMiles() = this / MILE

fun Long.toTimeDisplay(): String {
    val minutes: Long = (this / 1000) / 60
    val seconds: Long = (this / 1000) % 60
    return minutes.toString().padStart(2, '0') + ":" +
            seconds.toString().padStart(2, '0')
}