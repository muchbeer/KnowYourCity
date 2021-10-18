package raum.muchbeer.knowyourcity.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

@Database(entities = [Activity::class, Location::class,  ActivityLocationCrossRef::class],
    exportSchema = false,
    version = 1)
abstract class CityDatabase() : RoomDatabase() {

    abstract fun cityDao() : CityDao

    companion object {
        @Volatile
        private var INSTANCE: CityDatabase? = null

        fun getInstance(context: Context, callBack : Callback): CityDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityDatabase::class.java,
                    "city_database.db"
                )
                    .addCallback(callBack)
                    .build()
                INSTANCE = instance
                return instance
            }
        }

}

class CallBackInsertDb @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: Provider<CityDatabase>,
    @Named("db_coroutine") private val dbScope : CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        val dao = database.get().cityDao()
        dbScope.launch {

            val gson = GsonBuilder().create()
            var data = getJsonDataFromAsset(context, "activities.json")

            dao.insertActivities(gson.fromJson(data, Array<Activity>::class.java).toList())

            data = getJsonDataFromAsset(context, "locations.json")

          dao.insertLocations(gson.fromJson(data, Array<Location>::class.java).toList())

         data = getJsonDataFromAsset(context, "crossref.json")
         dao.insertActivityLocationCrossRefs(
                gson.fromJson(
                    data,
                    Array<ActivityLocationCrossRef>::class.java
                ).toList()
            )
        }
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}
}
