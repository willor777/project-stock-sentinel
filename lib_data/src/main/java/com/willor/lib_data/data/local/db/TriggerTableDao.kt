package com.willor.lib_data.data.local.db

import androidx.room.*
import com.willor.lib_data.domain.dataobjs.entities.TriggerEntity
import kotlinx.coroutines.flow.Flow


@Suppress("unused")
@Dao
interface TriggerTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrigger(triggerEntity: TriggerEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTriggers(vararg triggerEntity: TriggerEntity)

    @Delete
    suspend fun deleteTrigger(triggerEntity: TriggerEntity)
    
    
    @Query("SELECT * FROM ${StockDataDb.TRIGGER_TABLE}")
    fun getAllTriggers(): Flow<List<TriggerEntity>?>


    @Query("SELECT * FROM ${StockDataDb.TRIGGER_TABLE} WHERE id = :id")
    suspend fun getTriggerById(id: Int): TriggerEntity?


    @Query("SELECT * FROM ${StockDataDb.TRIGGER_TABLE} WHERE ticker = :ticker")
    fun getAllTriggersForTicker(ticker: String): Flow<List<TriggerEntity>?>


    @Query("SELECT * FROM ${StockDataDb.TRIGGER_TABLE} WHERE triggerValue > 0")
    fun getAllLongTriggers(): Flow<List<TriggerEntity>?>


    @Query("SELECT * FROM ${StockDataDb.TRIGGER_TABLE} WHERE triggerValue < 0")
    fun getAllShortTriggers(): Flow<List<TriggerEntity>?>


    @Query(
        "SELECT * FROM ${StockDataDb.TRIGGER_TABLE} WHERE timestamp BETWEEN :startTime AND :endTime"
    )
    fun getAllTriggersInTimeframe(startTime: Long, endTime: Long): Flow<List<TriggerEntity>?>


    @Query("SELECT * FROM ${StockDataDb.TRIGGER_TABLE} WHERE triggerValue > 0 AND ticker = :ticker")
    fun getAllLongTriggersForTicker(ticker: String): Flow<List<TriggerEntity>?>


    @Query("SELECT * FROM ${StockDataDb.TRIGGER_TABLE} WHERE triggerValue < 0 AND ticker = :ticker")
    fun getAllShortTriggersForTicker(ticker: String): Flow<List<TriggerEntity>?>


    @Query(
        "SELECT * FROM ${StockDataDb.TRIGGER_TABLE} WHERE ticker = :ticker " +
                "AND timestamp < :endTime AND timestamp > :startTime"
    )
    fun getAllTriggersForTickerInTimeframe(ticker: String, startTime: Long, endTime: Long)
    : Flow<List<TriggerEntity>?>


    @Query(
        "SELECT * FROM ${StockDataDb.TRIGGER_TABLE} WHERE ticker = :ticker AND triggerValue > 0 " +
                "AND timestamp BETWEEN :endTime AND  :startTime"
    )
    fun getAllLongTriggersForTickerInTimeframe(ticker: String, startTime: Long, endTime: Long)
    : Flow<List<TriggerEntity>?>


    @Query(
        "SELECT * FROM ${StockDataDb.TRIGGER_TABLE} WHERE ticker = :ticker AND triggerValue < 0 " +
                "AND timestamp BETWEEN :endTime AND  :startTime"
    )
    fun getAllShortTriggersForTickerInTimeframe(ticker: String, startTime: Long, endTime: Long)
    : Flow<List<TriggerEntity>?>

}
















