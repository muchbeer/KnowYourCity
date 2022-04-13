package raum.muchbeer.knowyourcity.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import raum.muchbeer.knowyourcity.data.*

class ITotalRepositoryImpl(val dao : TotalDao) : ITotalRepository {

        override fun getAllAgrievanceEntry(): LiveData<List<AgrienceModel>> {
            //The best tutorial to finalize the project
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

    override fun getAllDAttachByStatus(uploadStatus: IMAGESTATUS): Flow<List<DpapAttachEntity>> {
       return dao.getAllDAttachByStatus(uploadStatus)    }

    override fun retrieveAllDAttachmentUploads(): Flow<List<DpapAttachEntity>> {
           return dao.retrieveAllDAttachmentUploads()
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

    override suspend fun updateDattachment(dattach: DpapAttachEntity) {
       dao.updateDAttachment(dattach)
    }

    fun d_check_Upload() {
                dao.retrieveAllDAttachmentUploads().map { dpapEntinty ->
                   dpapEntinty.forEach {
                      val upload = it.image_status
                        uploadProcees(imageStatus = upload,
                                dattach = it)
                   }
            }
        }

    private suspend fun uploadProcees(imageStatus : IMAGESTATUS, dattach: DpapAttachEntity) {
        when (imageStatus) {
          IMAGESTATUS.NOT_AVAILABLE -> {

          }
            IMAGESTATUS.AVAILABLE -> {
                uploadResponseAndSetSuccesful(dattach)
            }
           IMAGESTATUS.SUCCESSFUL -> {

           }
        }
    }

    private suspend fun uploadResponseAndSetSuccesful(dattach: DpapAttachEntity) {
        val boolen = true
        if (boolen) {
            updateDattachment(dattach.copy(image_status = IMAGESTATUS.SUCCESSFUL))
        }
    }
}