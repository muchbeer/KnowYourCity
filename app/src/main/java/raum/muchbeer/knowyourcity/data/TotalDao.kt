package raum.muchbeer.knowyourcity.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TotalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAGrievance(agrievance : AgrienceModel) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBpapDetailEntry(papDetail: BpapDetailModel) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBpapDetailList(papDetail: List<BpapDetailModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCGrievanceEntry(cgrienvance: CgrievanceModel) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDAttachEntry(dAttach: DpapAttachEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCGrievanceList(dAttachs: List<DpapAttachEntity>)

    @Query("SELECT * FROM c_grievance WHERE a_username = :username")
    fun getAllGrieveWithPaps(username : String) : LiveData<List<CgrievanceModel>>

    @Query("SELECT * FROM d_papAttachEntity WHERE c_fullname = :fullName")
    fun getAllDpapsAttachmentInCGrievence(fullName: String) : LiveData<List<DpapAttachEntity>>

    @Query("SELECT * FROM d_papAttachEntity WHERE image_status = :imageStatus")
    fun getAllDAttachByStatus(imageStatus: IMAGESTATUS) : Flow<List<DpapAttachEntity>>

    @Query("SELECT * FROM b_grievance WHERE a_username =:username")
    fun getAllBpapsWithEachUsername(username: String) : LiveData<List<BpapDetailModel>>


    @Query("SELECT * FROM a_grievance")
    fun retrieveAgrievanceEntry() : LiveData<List<AgrienceModel>>

    @Query("SELECT * FROM c_grievance")
    fun retrieveCGrievance() : LiveData<List<CgrievanceModel>>

    @Query("SELECT * FROM d_papAttachEntity")
    fun retrieveAllDAttachment() : LiveData<List<DpapAttachEntity>>

    @Query("SELECT * FROM b_grievance")
    fun retrieveAllBPaps() : LiveData<List<BpapDetailModel>>

    @Query("SELECT * FROM d_papAttachEntity")
    fun retrieveAllDAttachmentUploads() : Flow<List<DpapAttachEntity>>

    @Update
    suspend fun updateCgrievance(cgrienvance: CgrievanceModel)

    @Update
    suspend fun updateDAttachment(dAttach: DpapAttachEntity)
}