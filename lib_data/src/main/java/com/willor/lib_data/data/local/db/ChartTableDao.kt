package com.willor.lib_data.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.willor.lib_data.domain.dataobjs.entities.StockChartEntity

@Dao
interface ChartTableDao {

    @Query("SELECT * FROM ${StockDataDb.CHART_TABLE} WHERE id = :id LIMIT 1")
    fun getById(id: String): StockChartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChart(stockChartEntity: StockChartEntity)
}