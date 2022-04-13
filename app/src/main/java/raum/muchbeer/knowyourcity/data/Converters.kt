package raum.muchbeer.knowyourcity.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(
        json,
        object : TypeToken<T>() {}.type
    )

    // ***********BGrievanceModel*****************

    @TypeConverter
    fun papDetailModelToJson(listOfPapIdentity: List<BpapDetailModel>): String {
        val gson = Gson()

        if (listOfPapIdentity.isNullOrEmpty()) return ""

        return gson.toJson(listOfPapIdentity)
    }

    @TypeConverter
    fun papDetailModelToPapModel(jsonPapDetail: String): List<BpapDetailModel> {
        if (jsonPapDetail.isEmpty()) return emptyList()

        return Gson().fromJson(jsonPapDetail)
    }

    // ***********CGrievanceModel*****************
    @TypeConverter
    fun papGrievanceDataToJson(listOfGrievance: List<CgrievanceModel>): String {
        val gson = Gson()

        if (listOfGrievance.isNullOrEmpty()) return ""

        return gson.toJson(listOfGrievance)
    }

    @TypeConverter
    fun papGrievanceDataToGModel(jsonGrievanceDetail: String): List<CgrievanceModel> {

        if (jsonGrievanceDetail.isEmpty()) return emptyList()

        return Gson().fromJson(jsonGrievanceDetail)
    }

    // ***********DAttachement*****************
    @TypeConverter
    fun papDAttachmentToJson(listOfAttachment: List<DpapAttachEntity>): String {
        val gson = Gson()

        if (listOfAttachment.isNullOrEmpty()) return ""

        return gson.toJson(listOfAttachment)
    }

    @TypeConverter
    fun papDAttachmentToGModel(jsonAttachment: String): List<DpapAttachEntity> {
        if (jsonAttachment.isEmpty()) return emptyList()

        return Gson().fromJson(jsonAttachment)
    }

    @TypeConverter
    fun fromImageStatus(imageStatus: IMAGESTATUS): String {
        return imageStatus.name
    }

    @TypeConverter
    fun toImageStatus(imageString: String): IMAGESTATUS {
        return IMAGESTATUS.valueOf(imageString)
    }
}