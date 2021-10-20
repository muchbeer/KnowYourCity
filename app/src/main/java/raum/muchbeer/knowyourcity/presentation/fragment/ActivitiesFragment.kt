package raum.muchbeer.knowyourcity.presentation.fragment

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import raum.muchbeer.knowyourcity.R
import raum.muchbeer.knowyourcity.data.GeofenceBroadcastReceiver
import raum.muchbeer.knowyourcity.data.GeofencingChanges
import raum.muchbeer.knowyourcity.databinding.FragmentActivityBinding
import raum.muchbeer.knowyourcity.presentation.fragment.LocationsFragment.Companion.RC_LOCATION
import raum.muchbeer.knowyourcity.presentation.viewmodel.activity.ActivitiesAdapter
import raum.muchbeer.knowyourcity.presentation.viewmodel.activity.ActivityVM
import raum.muchbeer.knowyourcity.presentation.viewmodel.activity.OnClickListener

@AndroidEntryPoint
class ActivitiesFragment : Fragment(), OnClickListener {

    private val viewModel : ActivityVM by viewModels()
    private lateinit var adapter: ActivitiesAdapter
    private lateinit var geofenceClient: GeofencingClient
    private var geofenceChange : GeofencingChanges? = null
    private var _binding: FragmentActivityBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val pendingIntent: PendingIntent by lazy {
        val intent = Intent(requireContext(), GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(requireContext(), 0, intent,
             PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentActivityBinding.inflate(inflater, container, false)

        geofenceClient = LocationServices.getGeofencingClient(requireActivity())
        adapter = ActivitiesAdapter(requireContext(), this)
        binding.listActivities.adapter = adapter
      //  binding.listActivities.layoutManager =
        retrieveActivities()

        return binding.root
    }

    private fun retrieveActivities() {
        viewModel.allActivities.observe(viewLifecycleOwner) {
            adapter.submitList(it)

            if (it.any { a -> a.geofenceEnabled } && checkPermissions().isEmpty()) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.activities_background_reminder),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.ok) {}
                    .show()
            }
           it.forEach { activity->
                Log.d("ActivityFragment", "The interested : ${activity.title}")
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(id: Int, title: String) {
        val action = ActivitiesFragmentDirections
            .actionActivitiesFragmentToLocationsFragment(id, "Locations with $title")

        val navController = Navigation.findNavController(requireView())
        navController.navigate(action)
    }

    override fun onGeofenceClick(id: Int)  {
        viewLifecycleOwner.lifecycleScope.launch {
            geofenceChange = viewModel.toggleGeofencing(id = id)
        }

        handleGeofencing()
    }

    @SuppressLint("InlinedApi")
    @AfterPermissionGranted(RC_LOCATION)
    fun handleGeofencing() {
        val neededPermissions = checkPermissions()
        if (neededPermissions.contains(ACCESS_FINE_LOCATION)) {
            requestPermission(
                R.string.activities_location_snackbar,
                R.string.locations_rationale,
                ACCESS_FINE_LOCATION
            )
        } else if (neededPermissions.contains(ACCESS_BACKGROUND_LOCATION)) {
            requestPermission(
                R.string.activities_background_snackbar,
                R.string.activities_background_rationale,
                ACCESS_BACKGROUND_LOCATION
            )
        } else {
            processGeofences()
        }
    }

    private fun requestPermission(
        @StringRes snackbarMessage: Int,
        @StringRes rationale: Int,
        permission: String
    ) {
        Snackbar.make(
            requireView(),
            getString(snackbarMessage),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.ok) {
                EasyPermissions.requestPermissions(
                    this,
                    getString(rationale),
                    RC_LOCATION,
                    permission
                )
            }
            .show()
    }

    @SuppressLint("MissingPermission")
    private fun processGeofences() {
        if (geofenceChange != null) {
            if (geofenceChange!!.idsToRemove.isNotEmpty()) {
                geofenceClient.removeGeofences(geofenceChange!!.idsToRemove)
            }

            if (geofenceChange!!.locationsToAdd.isNotEmpty()) {
                geofenceClient.addGeofences(getGeofencingRequest(), pendingIntent)
            }
        }
    }

    private fun getGeofencingRequest() =
        GeofencingRequest.Builder().apply {
            addGeofences(geofenceChange!!.locationsToAdd)
            setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER)
        }.build()

    private fun checkPermissions(): List<String> {
        val permissionsNeeded = ArrayList<String>()
        if (!EasyPermissions.hasPermissions(
                requireContext(),
                ACCESS_FINE_LOCATION
            )
        ) {
            permissionsNeeded.add(ACCESS_FINE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            !EasyPermissions.hasPermissions(
                requireContext(),
                ACCESS_BACKGROUND_LOCATION
            )
        ) {
            permissionsNeeded.add(ACCESS_BACKGROUND_LOCATION)
        }

        return permissionsNeeded
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //  super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }
}