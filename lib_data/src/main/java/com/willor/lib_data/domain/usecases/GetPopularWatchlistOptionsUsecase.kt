package com.willor.lib_data.domain.usecases

import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.popular_wl_options_resp.PopularWatchlistOptions
import kotlinx.coroutines.flow.Flow

class GetPopularWatchlistOptionsUsecase(
    private val repo: Repo
) {

    operator fun invoke(): Flow<DataResourceState<PopularWatchlistOptions?>> =
        repo.getAllAvailablePopularWatchlistOptions()

}