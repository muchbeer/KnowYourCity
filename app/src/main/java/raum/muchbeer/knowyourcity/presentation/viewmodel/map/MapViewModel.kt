package raum.muchbeer.knowyourcity.presentation.viewmodel.map

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import raum.muchbeer.knowyourcity.data.Region
import raum.muchbeer.knowyourcity.data.RegionWithPoints
import raum.muchbeer.knowyourcity.repository.ICityRepository
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
private val repository : ICityRepository
) : ViewModel() {

    private val TAG = MapViewModel::class.simpleName

    val allLocations = repository.getAllLocations()

    private val _allRegions = MutableLiveData<List<RegionWithPoints>>()
    val allRegionsLiveData : LiveData<List<RegionWithPoints>>
        get() = _allRegions

    private val visibleRegionIds = MutableLiveData<ArrayList<Int>>()
    val visibleRegionsIdsLiveData : LiveData<ArrayList<Int>>
        get() = visibleRegionIds

    init {
        visibleRegionIds.value = ArrayList(_allRegions.value!!.map { r -> r.region.id })
    }

    val beginCustomDraw = MutableLiveData<Boolean>(false)

    val selectedRegions: LiveData<List<Pair<Region, Boolean>>> =
        Transformations.switchMap(visibleRegionIds) { ids ->
            val list = arrayListOf<Pair<Region, Boolean>>()
            for (wrapper in _allRegions.value!!) {
                if (ids.isEmpty()) {
                    list.add(Pair(wrapper.region, false))
                } else {
                    list.add(Pair(wrapper.region, ids.contains(wrapper.region.id)))
                }
            }

            MutableLiveData(list.toList())
        }

    fun toggleVisibleRegion(id: Int?) {
        var currentIds: ArrayList<Int> = visibleRegionIds.value!!
        if (id == null) {
            visibleRegionIds.value = arrayListOf()
        } else {
            when {
                currentIds.isEmpty() -> currentIds = arrayListOf(id)

                currentIds.contains(id) -> currentIds.remove(id)

                !currentIds.contains(id) -> currentIds.add(id)
            }

            visibleRegionIds.value = currentIds
        }
    }

    fun setBeginCustomDraw(status: Boolean) {
        beginCustomDraw.value = status
    }

    fun mutableRegionId() = viewModelScope.launch {
        _allRegions.value = repository.getAllRegionsWithPoints()
    }
}