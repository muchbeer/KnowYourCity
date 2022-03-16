package raum.muchbeer.knowyourcity.data

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Entity(tableName = "a_grievance")
data class AgrienceModel(
    @PrimaryKey
    val primary_key : String,
    val user_name: String,
    @TypeConverters(Converters::class)
    val papdetails: List<BpapDetailModel>
)


@Parcelize
@Entity(tableName = "b_grievance")
data class BpapDetailModel(
    @PrimaryKey
    val b_pap_id: String,
    val a_username: String,
    @TypeConverters(Converters::class)
    val grievance: List<CgrievanceModel>,
    ) : Parcelable


@Parcelize
@Entity(tableName = "c_grievance")
data class CgrievanceModel(
    val agreetosign: String,
    @PrimaryKey(autoGenerate = false)
    val full_name : String,
    val a_username : String,
    @TypeConverters(Converters::class)
    val attachments: List<DpapAttachEntity> = listOf()
) : Parcelable



@Parcelize
@Entity(tableName = "d_papAttachEntity")
data class DpapAttachEntity(
    @PrimaryKey(autoGenerate = true)
    val d_pap_key : Int = 0,
    val file_name : String,
    val c_fullname: String,
    val url_name : String = ""

) : Parcelable
