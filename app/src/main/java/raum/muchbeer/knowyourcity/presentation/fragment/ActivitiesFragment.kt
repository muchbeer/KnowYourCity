package raum.muchbeer.knowyourcity.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import raum.muchbeer.knowyourcity.R
import raum.muchbeer.knowyourcity.databinding.FragmentActivityBinding
import raum.muchbeer.knowyourcity.presentation.viewmodel.activity.ActivitiesAdapter
import raum.muchbeer.knowyourcity.presentation.viewmodel.activity.ActivityVM
import raum.muchbeer.knowyourcity.presentation.viewmodel.activity.OnClickListener

@AndroidEntryPoint
class ActivitiesFragment : Fragment(), OnClickListener {

    private val viewModel : ActivityVM by viewModels()
    private lateinit var adapter: ActivitiesAdapter

    private var _binding: FragmentActivityBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentActivityBinding.inflate(inflater, container, false)
        adapter = ActivitiesAdapter(requireContext(), this)

        binding.listActivities.adapter = adapter
      //  binding.listActivities.layoutManager =
        sampleOutput()

        return binding.root
    }

    private fun sampleOutput() {
        viewModel.allActivities.observe(viewLifecycleOwner) {
            adapter.submitList(it)
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

    override fun onGeofenceClick(id: Int) {
        TODO("Not yet implemented")
    }
}