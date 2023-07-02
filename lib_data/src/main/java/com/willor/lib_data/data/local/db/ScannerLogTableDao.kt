package com.willor.lib_data.data.local.db

import androidx.room.*
import com.willor.lib_data.domain.dataobjs.entities.ScannerLogMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScannerLogTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(msgs: ScannerLogMessageEntity)

    @Query("SELECT * FROM ${StockDataDb.SCANNER_LOG_TABLE}")
    fun getAll(): Flow<List<ScannerLogMessageEntity>>

    @Query("SELECT * FROM ${StockDataDb.SCANNER_LOG_TABLE} WHERE timestamp > :startTime AND timestamp < :endTime")
    fun getAllWithinTimeSpan(startTime: Long, endTime: Long): Flow<List<ScannerLogMessageEntity>>

    @Query("DELETE FROM ${StockDataDb.SCANNER_LOG_TABLE}")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteLogMsg(msgs: ScannerLogMessageEntity)
}