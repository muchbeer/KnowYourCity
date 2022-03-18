package raum.muchbeer.knowyourcity.di

import android.content.Context
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import raum.muchbeer.knowyourcity.data.CityDao
import raum.muchbeer.knowyourcity.data.CityDatabase
import raum.muchbeer.knowyourcity.data.CityDatabase.*
import raum.muchbeer.knowyourcity.data.CityDatabase.Companion.getInstance
import raum.muchbeer.knowyourcity.data.ModelMapper
import raum.muchbeer.knowyourcity.data.TotalDao
import raum.muchbeer.knowyourcity.repository.ICityRepository
import raum.muchbeer.knowyourcity.repository.ICityRepositoryImpl
import raum.muchbeer.knowyourcity.repository.ITotalRepository
import raum.muchbeer.knowyourcity.repository.ITotalRepositoryImpl
import javax.inject.Named
import javax.inject.Singleton
import kotlin.contracts.Returns

@InstallIn(SingletonComponent::class)
@Module
object DbModule {


    @Singleton
    @Provides
    fun provideCityDatabase(
            @ApplicationContext  context : Context,
            @Named("db_coroutine") dbScope: CoroutineScope
            ) : CityDatabase {
        return getInstance(context, dbScope)
    }


    @Singleton
    @Provides
    fun provideCityDao(cityDatabase: CityDatabase) : CityDao {
        return cityDatabase.cityDao()
    }

    @Singleton
    @Provides
    fun provideTotalDao(cityDatabase: CityDatabase) : TotalDao {
        return cityDatabase.totalDao()
    }

    @Singleton
    @Provides
    @Named("db_coroutine")
    fun provideCallBackCoroutine() = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun provideModlelMapper(): ModelMapper {
        return ModelMapper()
    }

    @Singleton
    @Provides
    fun provideRepository(dao: CityDao) : ICityRepository {
      return  ICityRepositoryImpl(dao)
    }

    @Singleton
    @Provides
    fun provideTotalRepository(dao: TotalDao) : ITotalRepository {
        return ITotalRepositoryImpl(dao)
    }

}