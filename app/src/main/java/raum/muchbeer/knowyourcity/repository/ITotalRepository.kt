package raum.muchbeer.knowyourcity.repository

import androidx.lifecycle.LiveData
import raum.muchbeer.knowyourcity.data.*

interface ITotalRepository {

    fun getAllAgrievanceEntry() : LiveData<List<AgrienceModel>>

    fun getAllBpapsEntry() : LiveData<List<BpapDetailModel>>

    fun getAllCGrievanceEntry() : LiveData<List<CgrievanceModel>>

    fun getAllDpapsEntry() : LiveData<List<DpapAttachEntity>>

    fun getAllBGrievWithUsername(username: String) : LiveData<List<BpapDetailModel>>

    fun getAllCGrievanceWithUsername(username : String) : LiveData<List<CgrievanceModel>>

    fun getDAddAttachWithfullName(fullName: String) : LiveData<List<DpapAttachEntity>>

    suspend fun insertAgrievEntry(agrienceModel: AgrienceModel) : Long

    suspend fun insertBpapDetail(bpapsDetail : BpapDetailModel) : Long

    suspend fun insertCgrievDetail(cgriev : CgrievanceModel) : Long

    suspend fun insertDattach(dattach : DpapAttachEntity) : Long

    suspend fun updateCgrievance(cgriev: CgrievanceModel)

}