package raum.muchbeer.knowyourcity.repository

import androidx.lifecycle.LiveData
import raum.muchbeer.knowyourcity.data.*

class ITotalRepositoryImpl(val dao : TotalDao) : ITotalRepository {

    override fun getAllAgrievanceEntry(): LiveData<List<AgrienceModel>> {
      return dao.retrieveAgrievanceEntry()
    }

    override fun getAllBpapsEntry(): LiveData<List<BpapDetailModel>> {
       return dao.retrieveAllBPaps()
    }

    override fun getAllCGrievanceEntry(): LiveData<List<CgrievanceModel>> {
        return dao.retrieveCGrievance()
    }

    override fun getAllDpapsEntry(): LiveData<List<DpapAttachEntity>> {
        return dao.retrieveAllDAttachment()
    }

    override fun getAllBGrievWithUsername(username: String): LiveData<List<BpapDetailModel>> {
        return dao.getAllBpapsWithEachUsername(username = username)
    }

    override fun getAllCGrievanceWithUsername(username: String): LiveData<List<CgrievanceModel>> {
    return  dao.getAllGrieveWithPaps(username = username)    }

    override fun getDAddAttachWithfullName(fullName: String): LiveData<List<DpapAttachEntity>> {
        return dao.getAllDpapsAttachmentInCGrievence(fullName)
    }

    override suspend fun insertAgrievEntry(agrienceModel: AgrienceModel) : Long {
      return dao.insertAGrievance(agrievance = agrienceModel)
    }

    override suspend fun insertBpapDetail(bpapsDetail: BpapDetailModel)  : Long {
     return  dao.insertBpapDetailEntry(bpapsDetail)
    }

    override suspend fun insertCgrievDetail(cgriev: CgrievanceModel)  : Long{
      return  dao.insertCGrievanceEntry(cgriev)
    }

    override suspend fun insertDattach(dattach: DpapAttachEntity)  : Long{
      return dao.insertDAttachEntry(dattach)
    }

    override suspend fun updateCgrievance(cgriev: CgrievanceModel) {
        dao.updateCgrievance(cgriev)
    }


}