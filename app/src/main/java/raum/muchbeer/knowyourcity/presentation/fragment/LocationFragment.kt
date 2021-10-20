package raum.muchbeer.knowyourcity.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import raum.muchbeer.knowyourcity.R
import raum.muchbeer.knowyourcity.databinding.FragmentLocationBinding
import raum.muchbeer.knowyourcity.presentation.viewmodel.location.LocationAdapter
import raum.muchbeer.knowyourcity.presentation.viewmodel.location.LocationVM

@AndroidEntryPoint
class LocationFragment : Fragment() {

    private lateinit var adapter : LocationAdapter
    private val  viewModel : LocationVM by viewModels()

    private var _binding : FragmentLocationBinding?= null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLocationBinding.inflate(inflater, container, false)

        adapter = LocationAdapter()

        arguments?.let { bundle ->
            val passedArguments = LocationFragmentArgs.fromBundle(bundle)
            viewModel.getLocation(passedArguments.locationId)
                .observe(viewLifecycleOwner)  { wrapper ->
                    val location = wrapper.location
                    binding.title.text = location.title
                    binding.hours.text = location.hours
                    binding.description.text = location.description
                    binding.listActivities.adapter = adapter

                    adapter.submitList(wrapper.activities.sortedBy {
                        a -> a.title
                    })

                }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}