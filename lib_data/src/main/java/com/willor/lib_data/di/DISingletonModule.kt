package com.willor.lib_data.di

import android.content.Context
import androidx.room.Room
import com.willor.lib_data.data.RepoImpl
import com.willor.lib_data.data.local.db.StockDataDb
import com.willor.lib_data.data.local.prefs.DatastorePreferencesManager
import com.willor.lib_data.data.remote.RetrofitApi
import com.willor.lib_data.data.remote.StockDataService
import com.willor.lib_data.domain.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


// TODO Provide the Repo with Datastore Preferences


@Module
@InstallIn(SingletonComponent::class)
object DISingletonModule {


    @Singleton
    @Provides
    fun provideStockDataService(): StockDataService{
        return RetrofitApi.stockDataService
    }


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): StockDataDb{
        return Room.databaseBuilder(context, StockDataDb::class.java, StockDataDb.DB_NAME)
            .build()
    }


    @Singleton
    @Provides
    fun provideDatastorePreferences(
        @ApplicationContext context: Context
    ): DatastorePreferencesManager{
        return DatastorePreferencesManager(context)
    }


    @Singleton
    @Provides
    fun provideRepo(
        stockDataService: StockDataService,
        database: StockDataDb,
        prefs: DatastorePreferencesManager
        ): Repo {
        return RepoImpl(stockDataService, database, prefs)
    }
}