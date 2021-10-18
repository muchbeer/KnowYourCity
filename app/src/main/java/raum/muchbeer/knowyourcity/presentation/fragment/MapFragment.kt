package raum.muchbeer.knowyourcity.presentation.fragment

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import raum.muchbeer.knowyourcity.R
import raum.muchbeer.knowyourcity.databinding.FragmentMapBinding
import raum.muchbeer.knowyourcity.presentation.viewmodel.MapViewModel

@AndroidEntryPoint
class MapFragment : Fragment() {
    private val mapViewModel : MapViewModel by viewModels()

    private var _binding : FragmentMapBinding?= null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMapBinding.inflate(inflater, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync { map ->
            val bay = LatLng(37.68, -122.42)
            map.moveCamera(CameraUpdateFactory.zoomTo(10f))
            map.moveCamera(CameraUpdateFactory.newLatLng(bay))
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isTiltGesturesEnabled = false

            mapViewModel.allLocations.observe(viewLifecycleOwner) { locations ->
                locations.forEach { location ->
                    val point = LatLng(location.latitude, location.longitude)
                    val marker = map.addMarker(
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
                    marker.tag = location.locationId
                }
            }
            map.setOnInfoWindowClickListener { marker ->
                val action = MapFragmentDirections
                    .actionMapFragmentToLocationFragment(locationId = marker.tag as Int)

                val navController = Navigation.findNavController(requireView())
                navController.navigate(action)
            }
        }


        return binding.root
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
}