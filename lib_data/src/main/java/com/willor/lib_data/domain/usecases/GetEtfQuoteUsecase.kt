package com.willor.lib_data.domain.usecases

import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.etf_quote_resp.EtfQuote
import kotlinx.coroutines.flow.Flow

class GetEtfQuoteUsecase(
    private val repo: Repo
) {

    operator fun invoke(ticker: String): Flow<DataResourceState<EtfQuote?>> {
        return repo.getEtfQuote(ticker)
    }
}