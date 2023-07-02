package com.willor.lib_data.domain.usecases

import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.major_futures_resp.MajorFutures
import kotlinx.coroutines.flow.Flow

class GetMajorFuturesUsecase(
    private val repo: Repo
) {
    operator fun invoke(): Flow<DataResourceState<MajorFutures?>> {
        return repo.getMajorFutures()
    }
}