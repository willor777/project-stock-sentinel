package com.willor.lib_data.domain.usecases

import com.github.willor777.stock_analysis_lib.charts.StockChart
import com.github.willor777.stock_analysis_lib.common.CandleInterval
import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.responses.chart_resp.StockChart.Companion.convertToAnalysisStockChart

class GetStockChartsForAnalysisUsecase(
    val repo: Repo
) {

    suspend operator fun invoke(
        tickers: List<String>,
        candleInterval: String,
        periodRange: String,
        prepost: Boolean
    ): List<StockChart>{

        val charts = mutableListOf<StockChart>()
        for (t in tickers) {

            // Get chart from api
            val c = repo.getStockChart(
                t, candleInterval, periodRange, prepost
            ) ?: continue

            // Convert chart to analysis chart
            val analysisChart = c.convertToAnalysisStockChart()
            charts.add(analysisChart)
        }

        return charts
    }


}