package com.willor.lib_data.domain.usecases

import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.entities.TriggerEntity
import kotlinx.coroutines.flow.Flow

class GetTriggersUsecase(
    val repo: Repo
) {
    operator fun invoke(
        id: Int? = null,
        fromTime: Long? = null,
        toTime: Long? = null,
        ticker: String? = null,
        onlyLong: Boolean = false,
        onlyShort: Boolean = false,
    ): Flow<List<TriggerEntity>?> {
        return repo.getTriggers(id, fromTime, toTime, ticker, onlyLong, onlyShort)
    }
}