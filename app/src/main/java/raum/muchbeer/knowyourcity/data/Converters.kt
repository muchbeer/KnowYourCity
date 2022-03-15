package raum.muchbeer.knowyourcity.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {

    inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(
        json,
        object : TypeToken<T>() {}.type
    )

    // ***********BGrievanceModel*****************

    @TypeConverter
    fun papDetailModelToJson(listOfpapIdentity: List<BpapDetailModel>): String {
        val gson = Gson()
        val gsonPretty = GsonBuilder().setPrettyPrinting()
            .create()

        if (listOfpapIdentity.isNullOrEmpty()) return ""

        val papIDJson = gson.toJson(listOfpapIdentity)
        return papIDJson
    }

    @TypeConverter
    fun papDetailModelToPapModel(jsonPapDetail: String): List<BpapDetailModel> {
        if (jsonPapDetail.isNullOrEmpty()) return emptyList()

        val type: Type = object : TypeToken<List<BpapDetailModel?>?>() {}.type
        val paplistEntries: List<BpapDetailModel> =
            Gson().fromJson<List<BpapDetailModel>>(jsonPapDetail)
        return paplistEntries
    }

    // ***********CGrievanceModel*****************
    @TypeConverter
    fun papGrievanceDataToJson(listOfgrievance: List<CgrievanceModel>): String {
        val gson = Gson()

        if (listOfgrievance.isNullOrEmpty()) return ""

        val papIDJson = gson.toJson(listOfgrievance)
        return papIDJson
    }

    @TypeConverter
    fun papGrievanceDataToGModel(jsonGrievanceDetail: String): List<CgrievanceModel> {
        val gson = Gson()

        if (jsonGrievanceDetail.isNullOrEmpty()) return emptyList()

        val type: Type = object : TypeToken<List<CgrievanceModel?>?>() {}.type
        val paplistEntries: List<CgrievanceModel> =
            Gson().fromJson<List<CgrievanceModel>>(jsonGrievanceDetail)

        return paplistEntries
    }

    // ***********DAttachement*****************
    @TypeConverter
    fun papDAttachmentToJson(listOfAttachment: List<DpapAttachEntity>): String {
        val gson = Gson()

        if (listOfAttachment.isNullOrEmpty()) return ""

        val papAttachJson = gson.toJson(listOfAttachment)
        return papAttachJson
    }

    @TypeConverter
    fun papDAttachmentToGModel(jsonAttachment: String): List<DpapAttachEntity> {
        val gson = Gson()

        if (jsonAttachment.isNullOrEmpty()) return emptyList()

        val type: Type = object : TypeToken<List<CgrievanceModel?>?>() {}.type
        val listAttached: List<DpapAttachEntity> =
            Gson().fromJson<List<DpapAttachEntity>>(jsonAttachment)

        return listAttached
    }
}