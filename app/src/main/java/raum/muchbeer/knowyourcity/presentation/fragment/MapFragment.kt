package raum.muchbeer.knowyourcity.presentation.fragment

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.motion.widget.Key.CUSTOM
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.toColorInt
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import raum.muchbeer.knowyourcity.R
import raum.muchbeer.knowyourcity.databinding.FragmentMapBinding
import raum.muchbeer.knowyourcity.presentation.fragment.LocationsFragment.Companion.RC_LOCATION
import raum.muchbeer.knowyourcity.presentation.viewmodel.map.MapViewModel

@AndroidEntryPoint
class MapFragment : Fragment() {
    private val mapViewModel : MapViewModel by viewModels()
    private lateinit var googleMap : GoogleMap
    private var _binding : FragmentMapBinding?= null
    private val binding
        get() = _binding!!


    private lateinit var polyline: Polyline
    private val polygons = arrayListOf<Polygon>()
    private val markers = arrayListOf<Marker>()
    private var drawing = false
    private val drawPoints = arrayListOf<LatLng>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMapBinding.inflate(inflater, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync { map ->
            googleMap = map

            configureMap()
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun configureMap() {
        val bay = LatLng(-6.766620, 39.273226)
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(10f))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bay))
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isTiltGesturesEnabled = false

        loadLocations()
        loadRegions()

        googleMap.setOnInfoWindowClickListener { marker ->
            val action = MapFragmentDirections
                .actionMapFragmentToLocationFragment(locationId = marker.tag as Int)

            val navController = Navigation.findNavController(requireView())
            navController.navigate(action)
        }
        setMapStyle()
        enableMyLocation()
    }

    private fun loadLocations() {
        mapViewModel.allLocations.observe(viewLifecycleOwner) { locations ->
            locations.forEach { location ->
                val point = LatLng(location.latitude, location.longitude)
                val marker = googleMap.addMarker(
                    MarkerOptions()
                        .position(point)
                        .title(location.title)
                        .snippet("Hours: ${location.hours}")
                        .icon(
                            getBitmapFromVector(
                                R.drawable.ic_star_black_24dp,
                                R.color.purple_500
                            )
                        )
                        .alpha(0.75f)
                )
                marker?.tag = location.locationId
                markers.add(marker!!)
                googleMap.addCircle(
                    CircleOptions()
                        .center(point)
                        .radius(location.geofenceRadius.toDouble())
                )
            }
        }
    }

    private fun loadRegions() {
        //Now insert value
        mapViewModel.mutableRegionId()

        //Then you observe down
        mapViewModel.allRegionsLiveData.observe(viewLifecycleOwner)  { regionWthPoints ->
            regionWthPoints.forEach {
                val points = it.points.map { regionPoint ->
                    LatLng(
                        regionPoint.latitude,
                        regionPoint.longitude
                    )
                }

                if (points.isNotEmpty()) {
                    val polygon = googleMap.addPolygon(
                        PolygonOptions()
                            .addAll(points)
                            .fillColor(it.region.color.toColorInt())
                            .strokeWidth(0f)
                    )
                    polygon.tag = it.region.id
                    polygons.add(polygon)
                } else {
                    Snackbar.make(binding.root, "No points Detected", Snackbar.LENGTH_LONG).show()
                }
            }

            //Then observe the values
            mapViewModel.visibleRegionsIdsLiveData.observe(viewLifecycleOwner) { regionIds ->

                polygons.forEach { p ->
                    if (p.tag == CUSTOM) {
                        p.isVisible = regionIds.isEmpty()
                    } else {
                        p.isVisible = regionIds.contains(p.tag)
                    }
                }

                filterLocations()
            }
        }
    }

    private fun setMapStyle() {
        var style = R.raw.map_style_light
        val mode = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        if (mode == Configuration.UI_MODE_NIGHT_YES) {
            style = R.raw.map_style_dark
        }

        //If we were to load the style from the response json from the api we would use string
        //instead of raw
        //this will be loadString
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), style))
    }

    private fun filterLocations() {

        markers.forEach { m ->
            m.isVisible =
                polygons.any { p ->
                    p.isVisible &&  p.points.contains(m.position)                }
        }
    }
    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(RC_LOCATION)
    private fun enableMyLocation() {
        if(EasyPermissions.hasPermissions(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            googleMap.isMyLocationEnabled = true
        } else {
            Snackbar.make(
                requireView(),
                getString(R.string.map_snackbar),
                Snackbar.LENGTH_INDEFINITE
            ).setAction(
                R.string.ok
            ) {
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.map_rationale),
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

    private fun getBitmapFromVector(
        @DrawableRes vectorResourceId: Int,
        @ColorRes colorResourceId: Int
    ): BitmapDescriptor {
        val vectorDrawable = resources.getDrawable(vectorResourceId, requireContext().theme)
            ?: return BitmapDescriptorFactory.defaultMarker()

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(
            vectorDrawable,
            ResourcesCompat.getColor(
                resources,
                colorResourceId, requireContext().theme
            )
        )
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_filter -> {
                val action = MapFragmentDirections.actionMapFragmentToFilterDialogFragment()
                val navController = Navigation.findNavController(requireView())
                navController.navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }
}