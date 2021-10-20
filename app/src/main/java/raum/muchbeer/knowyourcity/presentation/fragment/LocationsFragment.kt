package raum.muchbeer.knowyourcity.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import raum.muchbeer.knowyourcity.R
import raum.muchbeer.knowyourcity.data.GeofenceBroadcastReceiver
import raum.muchbeer.knowyourcity.databinding.FragmentLocationsBinding
import raum.muchbeer.knowyourcity.presentation.viewmodel.location.LocationVM
import raum.muchbeer.knowyourcity.presentation.viewmodel.location.LocationsAdapter
import raum.muchbeer.knowyourcity.presentation.viewmodel.location.OnLocationClickListener

@AndroidEntryPoint
class LocationsFragment : Fragment(), OnLocationClickListener {

    private lateinit var binding : FragmentLocationsBinding
    private val viewModel : LocationVM by viewModels()
    private lateinit var adapter: LocationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLocationsBinding.inflate(inflater, container, false)

       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = LocationsAdapter(this)
        binding.listLocations.adapter = adapter

        arguments?.let { bundle ->
            val passedArguments = LocationsFragmentArgs.fromBundle(bundle)
            if (passedArguments.activityId == 0) {
                viewModel.allLocations.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            } else {
                viewModel.locationsWithActivity(passedArguments.activityId)
                    .observe(viewLifecycleOwner)  {
                        adapter.submitList(it.locations)
                    }
            }
        }

        currentLocation()
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(RC_LOCATION)
    private fun currentLocation() {
        if(EasyPermissions.hasPermissions(requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            val fusedLocation = LocationServices.getFusedLocationProviderClient(requireActivity())

            fusedLocation.lastLocation.addOnSuccessListener {  location ->
                location?.let {  loc ->
                    adapter.setCurrentLocation(loc)
                }
            }
        } else {
            Snackbar.make(
                binding.linearLayout,
                getString(R.string.location_snackbar),
                Snackbar.LENGTH_INDEFINITE
            ).setAction(
                R.string.ok
            ) {
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_ask),
                    RC_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )

            }.show()
        }
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

    companion object {
        const val RC_LOCATION = 2016
        val BROADCASTrECEIVER = GeofenceBroadcastReceiver::class.qualifiedName
    }


    override fun onClick(id: Int) {
        val action = LocationsFragmentDirections
            .actionLocationsFragmentToLocationFragment(id)
     //   action.locationId = id
        val navController = Navigation.findNavController(requireView())
        navController.navigate(action)
    }
}