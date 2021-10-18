package raum.muchbeer.knowyourcity.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import raum.muchbeer.knowyourcity.R
import raum.muchbeer.knowyourcity.databinding.FragmentLocationsBinding
import raum.muchbeer.knowyourcity.presentation.viewmodel.location.LocationVM
import raum.muchbeer.knowyourcity.presentation.viewmodel.location.LocationsAdapter
import raum.muchbeer.knowyourcity.presentation.viewmodel.location.OnLocationClickListener

@AndroidEntryPoint
class LocationsFragment : Fragment(), OnLocationClickListener {

    private var _binding : FragmentLocationsBinding? = null
    private val viewModel : LocationVM by viewModels()
    private lateinit var adapter: LocationsAdapter

    private val binding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
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
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(id: Int) {
        val action = LocationsFragmentDirections
            .actionLocationsFragmentToLocationFragment(id)
     //   action.locationId = id
        val navController = Navigation.findNavController(requireView())
        navController.navigate(action)
    }
}