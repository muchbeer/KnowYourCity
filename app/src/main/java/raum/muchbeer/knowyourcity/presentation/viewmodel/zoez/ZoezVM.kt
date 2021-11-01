package raum.muchbeer.knowyourcity.presentation.viewmodel.zoez

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import raum.muchbeer.knowyourcity.repository.ICityRepository
import javax.inject.Inject

@HiltViewModel
class ZoezVM @Inject constructor(
private val repository : ICityRepository
) : ViewModel() {

}