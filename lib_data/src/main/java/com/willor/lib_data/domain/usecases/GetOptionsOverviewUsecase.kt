package com.willor.lib_data.domain.usecases

import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.options_overview_resp.OptionsOverview
import kotlinx.coroutines.flow.Flow

class GetOptionsOverviewUsecase(
    private val repo: Repo
) {

    operator fun invoke(ticker: String): Flow<DataResourceState<OptionsOverview?>> {
        return repo.getOptionsOverview(ticker)
    }
}