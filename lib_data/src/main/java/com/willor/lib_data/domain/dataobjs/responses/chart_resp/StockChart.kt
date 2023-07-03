package com.willor.lib_data.domain.dataobjs.responses.chart_resp


import android.util.Log
import com.github.willor777.stock_analysis_lib.common.CandleInterval
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class StockChart(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("lastUpdated")
    val lastUpdated: Long
){
    companion object {
//
        fun StockChart.convertToAnalysisStockChart(): com.github.willor777.stock_analysis_lib.charts.StockChart {

            val chart = this.data

            // Convert dates
            val dateList = convertDateTimeList(chart.datetime)

            // Build Analysis chart and return
            return com.github.willor777.stock_analysis_lib.charts.StockChart(
                chart.ticker,
                determineCandleInterval(chart.interval),
                chart.periodRange,
                chart.prepost,
                dateList,
                chart.timestamp,
                chart.open,
                chart.high,
                chart.low,
                chart.close,
                chart.volume
            )


        }

        private fun convertDateTimeList(datetimeList: List<String>): List<Date> {

            val dateList = mutableListOf<Date>()

            // String format of date...
            // 'Jun 30, 2023, 8:00:00 AM'
            for (sd in datetimeList){
                val dateFormat = "MMM dd, yyyy, hh:mm:ss aa"
                val sdf = SimpleDateFormat(dateFormat)
                dateList.add(
                    sdf.parse(sd)!!
                )
            }
            return dateList
        }

        private fun determineCandleInterval(stringInterval: String): CandleInterval {
            return when (stringInterval) {
                "1m" -> CandleInterval.M1
                "5m" -> CandleInterval.M5
                "15m" -> CandleInterval.M15
                "30m" -> CandleInterval.M30
                "1h" -> CandleInterval.H1
                "1d" -> CandleInterval.D1
                else -> {
                    Log.d("StockChart", "determineCandleInterval() 'else' block triggered" +
                            " on: $stringInterval")
                    CandleInterval.D1
                }
            }
        }
    }
}