package com.willor.lib_data.domain.usecases

import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.stock_competitors_resp.StockCompetitors
import kotlinx.coroutines.flow.Flow

class GetStockCompetitorsUsecase(
    val repo: Repo
) {

    operator fun invoke(ticker: String): Flow<DataResourceState<StockCompetitors?>> =
        repo.getStockCompetitors(ticker)
}