package raum.muchbeer.knowyourcity.data

import android.content.Context
import android.util.Log
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider
private val TAG = CityDatabase::class.simpleName

@Database(entities = [Activity::class, Location::class,  ActivityLocationCrossRef::class,
    Region::class, RegionPoint::class, Workout::class, WorkoutPoint::class,
    AgrienceModel::class, BpapDetailModel::class, CgrievanceModel::class, DpapAttachEntity::class],
    exportSchema = false,
    version = 16)
@TypeConverters(Converters::class)
abstract class CityDatabase() : RoomDatabase() {


    abstract fun cityDao() : CityDao
    abstract fun totalDao() : TotalDao

    companion object {
        @Volatile
        private var INSTANCE: CityDatabase? = null

        fun getTotalInstance(context: Context) : CityDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instanceTotal = Room.databaseBuilder(
                    context.applicationContext,
                    CityDatabase::class.java,
                    "total_database.db"
                ).build()
                INSTANCE = instanceTotal
                return instanceTotal
            }
        }

        fun getInstance(context: Context, dbScope: CoroutineScope): CityDatabase {
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
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val dao = getInstance(context, dbScope).cityDao()
                            dbScope.launch {
                                val gson = GsonBuilder().create()
                                var data: String? = null
                                try {
                                    data = context.assets.open("data.json").bufferedReader().use { it.readText()
                                    }
                                    Log.d("CityDatabase", "tHE data out is: : ${data}")
                                } catch (ioException: IOException) {
                                    Log.d("CityDatabase", "tHE json error is : ${ioException.message}")
                                    ioException.printStackTrace()
                                }

                                if (data != null) {
                                    val records = gson.fromJson(data, DatabaseContents::class.java)
                                    dao.insertActivities(records.activities)
                                    dao.insertActivityLocationCrossRefs(records.crossrefs)
                                    dao.insertLocations(records.locations)
                                    dao.insertRegions(records.regions)
                                    dao.insertRegionPoints(records.regionpoints)
                                }
                            }
                            Log.d(TAG, "Entered Callback23")
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }
}



