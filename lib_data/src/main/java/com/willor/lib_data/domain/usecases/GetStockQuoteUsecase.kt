package com.willor.lib_data.domain.usecases

import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.stock_quote_resp.StockQuote
import kotlinx.coroutines.flow.Flow

class GetStockQuoteUsecase(
    private val repo: Repo
) {

    operator fun invoke(ticker: String): Flow<DataResourceState<StockQuote?>> {
        return repo.getStockQuote(ticker)
    }
}