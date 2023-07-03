package com.willor.lib_data.domain.usecases

import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.chart_resp.StockChart
import kotlinx.coroutines.flow.Flow

class GetStockChartFlowUsecase(
    val repo: Repo
) {

    operator fun invoke(
        ticker: String, interval: String, periodRange: String, prepost: Boolean
    ): Flow<DataResourceState<StockChart?>> =
        repo.getStockChartFlow(ticker, interval, periodRange, prepost)
}