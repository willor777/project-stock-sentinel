package com.willor.lib_data.domain.usecases

import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.stock_snr_levels_resp.StockSnrLevels
import kotlinx.coroutines.flow.Flow

class GetSnrLevelsUsecase(
    private val repo: Repo
) {

    operator fun invoke(ticker: String): Flow<DataResourceState<StockSnrLevels?>> {
        return repo.getStockSnrLevels(ticker)
    }
}