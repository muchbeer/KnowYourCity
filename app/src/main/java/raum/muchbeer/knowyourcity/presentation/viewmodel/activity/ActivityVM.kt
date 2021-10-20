package raum.muchbeer.knowyourcity.presentation.viewmodel.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import raum.muchbeer.knowyourcity.data.GeofencingChanges
import raum.muchbeer.knowyourcity.repository.ICityRepository
import javax.inject.Inject

@HiltViewModel
class ActivityVM @Inject constructor(
    private val repository: ICityRepository
) :  ViewModel(){

    val allActivities = repository.getAllActivities()

   suspend fun toggleGeofencing(id: Int)  : GeofencingChanges {
        return repository.toggleActivityGeofence(id)
    }
}