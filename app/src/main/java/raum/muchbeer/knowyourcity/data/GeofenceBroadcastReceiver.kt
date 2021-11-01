package raum.muchbeer.knowyourcity.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.CallSuper
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import raum.muchbeer.knowyourcity.R
import raum.muchbeer.knowyourcity.presentation.fragment.LocationFragmentArgs
import raum.muchbeer.knowyourcity.presentation.fragment.LocationsFragment.Companion.BROADCASTrECEIVER
import raum.muchbeer.knowyourcity.presentation.fragment.LocationsFragmentDirections
import raum.muchbeer.knowyourcity.repository.ICityRepository
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val LOG_TAG = GeofenceBroadcastReceiver::class.simpleName

    @Inject lateinit var repository: ICityRepository

    @Inject @Named("db_coroutine")  lateinit var dbScope : CoroutineScope



   override fun onReceive(context: Context?, intent: Intent?) {

        val event = GeofencingEvent.fromIntent(intent)

        if (event.hasError()) {
            Log.d(LOG_TAG, "THe are error sending notification")
            return
        }

        if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val geofence = event.triggeringGeofences[0]
            Log.d(LOG_TAG, "The notification has entered the Geofence is about to enter")

            dbScope.launch {
                sendNotification(context!!, geofence.requestId.toInt())
            }

        }
    }

    private suspend fun sendNotification(context: Context, locationId: Int) {

            val  location = repository.getLocationById(locationId)
            val message =
                "Visit ${location.title} to enjoy your favorite activities"
            Log.d(BROADCASTrECEIVER, "tHE message is ${message}")

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "locations",
                    "Nearby Locations",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            val locationArgs =
                LocationsFragmentDirections.actionLocationsFragmentToLocationFragment(locationId).arguments

          //  LocationFragmentArgs.Builder().setLocationId(locationId).build().toBundle()

            val intent = NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.locationFragment)
                .setArguments(locationArgs)
                .createPendingIntent()

            val notification = NotificationCompat.Builder(context, "locations")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Discover the outdoors near you now!")
                .setContentText(message)
                .setContentIntent(intent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(1, notification)


    }

}

abstract class HiltBroadcastReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context?, intent: Intent?) {}
}