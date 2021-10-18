package raum.muchbeer.knowyourcity.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import raum.muchbeer.knowyourcity.repository.ICityRepository
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
private val repository : ICityRepository
) : ViewModel() {

    val allLocations = repository.getAllLocations()
}