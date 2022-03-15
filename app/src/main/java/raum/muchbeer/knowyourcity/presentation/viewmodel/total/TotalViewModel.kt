package raum.muchbeer.knowyourcity.presentation.viewmodel.total

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import raum.muchbeer.knowyourcity.data.AgrienceModel
import raum.muchbeer.knowyourcity.data.BpapDetailModel
import raum.muchbeer.knowyourcity.data.CgrievanceModel
import raum.muchbeer.knowyourcity.data.DpapAttachEntity
import raum.muchbeer.knowyourcity.repository.ITotalRepository
import javax.inject.Inject

@HiltViewModel
class TotalViewModel @Inject constructor(private  val repository : ITotalRepository)
                : ViewModel() {

    private val _pKValue = MutableLiveData<String>()
    val pkValue : LiveData<String>
        get() = _pKValue

    val allAgrienceEntry = repository.getAllAgrievanceEntry()

    val allBpapsEntry = repository.getAllBpapsEntry()

    val allCpapsDetailEntry = repository.getAllCGrievanceEntry()

    val allDAttachmentEntry = repository.getAllDpapsEntry()

    fun getAllBGrievWithSameUsername(username : String) : LiveData<List<BpapDetailModel>> {
        return repository.getAllBGrievWithUsername(username = username)
    }

    fun getAllCGrievWithSameUsername(username : String) : LiveData<List<CgrievanceModel>> {
        return repository.getAllCGrievanceWithUsername(username)
    }

    fun getAllDAttachWithfullName(fullName: String) : LiveData<List<DpapAttachEntity>> {
        return repository.getDAddAttachWithfullName(fullName)
    }

    fun insertAgrievEntry(agriev : AgrienceModel) = viewModelScope.launch {
       val aGrieValue = repository.insertAgrievEntry(agriev)
        if (aGrieValue >= 0) {
            Log.d(TAG, "Inserted AgrienceModel successfully record no: ${aGrieValue}")
        }
    }

    fun insertBpapsEntry(bpaps: BpapDetailModel) = viewModelScope.launch {
      val bPapEntryValue =  repository.insertBpapDetail(bpapsDetail = bpaps)
        if (bPapEntryValue >= 0) {
            Log.d(TAG, "Inserted BpapDetailModel successfully record no: ${bPapEntryValue}")
        }
    }

    fun insertCgriev(cgriev : CgrievanceModel) = viewModelScope.launch {
      val cGrievDetailValue =  repository.insertCgrievDetail(cgriev = cgriev)
        if (cGrievDetailValue >= 0) {
            Log.d(TAG, "Inserted CGrievance successfully record no: ${cGrievDetailValue}")
        }
    }

    fun insertDattach(dattach : DpapAttachEntity) = viewModelScope.launch {
      val dAttachValue =  repository.insertDattach(dattach = dattach)
        if (dAttachValue >= 0) {
            Log.d(TAG, "Inserted DpapAttachEntity successfully record no: ${dAttachValue}")
        }
    }

    fun setPkvalue(pkValue : String) {
        _pKValue.value = pkValue
    }

    fun deletallDattachment() = viewModelScope.launch {
        repository.deletallDattachmment()
    }

    fun deletallCgrievance() = viewModelScope.launch {
        repository.deletallCgrievance()
    }

    fun deletallBpaps() = viewModelScope.launch {
        repository.deletallBpaps()
    }

    companion object {
        private val TAG = TotalViewModel::class.simpleName
    }
}