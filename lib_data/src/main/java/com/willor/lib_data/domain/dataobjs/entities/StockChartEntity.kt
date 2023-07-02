package com.willor.lib_data.domain.dataobjs.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.willor.lib_data.data.local.db.StockDataDb
import com.willor.lib_data.domain.dataobjs.responses.chart_resp.StockChart
import com.willor.lib_data.utils.gson


@Entity(tableName = StockDataDb.CHART_TABLE)
data class StockChartEntity(
    @PrimaryKey val id: String,
    @ColumnInfo val ticker: String,
    @ColumnInfo val interval: String,
    @ColumnInfo val periodRange: String,
    @ColumnInfo val prepost: Boolean,
    @ColumnInfo val data: String,
    @ColumnInfo val timestamp: Long = System.currentTimeMillis(),

    ){
    companion object{
        fun buildStorageKey(
            ticker: String, interval: String, periodRange: String, prepost: Boolean): String{
            return "$ticker-$interval-$periodRange-$prepost"
        }
        
        fun fromStockChartResponse(chartResp: StockChart): StockChartEntity{
            return StockChartEntity(
                id = buildStorageKey(
                    chartResp.data.ticker,
                    chartResp.data.interval,
                    chartResp.data.periodRange,
                    chartResp.data.prepost
                ),
                ticker = chartResp.data.ticker,
                interval = chartResp.data.interval,
                periodRange = chartResp.data.periodRange,
                prepost = chartResp.data.prepost,
                data = gson.toJson(chartResp)
            )
        }
        
        fun toStockChartResponse(stockChartEntity: StockChartEntity): StockChart{
            return gson.fromJson(stockChartEntity.data, StockChart::class.java)
        }
    }
}
