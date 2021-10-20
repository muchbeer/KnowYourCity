package raum.muchbeer.knowyourcity.presentation.viewmodel.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import raum.muchbeer.knowyourcity.data.ActivityWithLocations
import raum.muchbeer.knowyourcity.data.LocationWithActivities
import raum.muchbeer.knowyourcity.repository.ICityRepository
import javax.inject.Inject

@HiltViewModel
class LocationVM @Inject constructor(
 private val repository: ICityRepository
) : ViewModel() {

 fun getLocation(locationId: Int) : LiveData<LocationWithActivities>  {
   return repository.getLocationWithActivities(locationId)
 }

 val allLocations = repository.getAllLocations()

 fun locationsWithActivity(activityId: Int) : LiveData<ActivityWithLocations> {
  return repository.getActivityWithLocations(activityId)
 }


}