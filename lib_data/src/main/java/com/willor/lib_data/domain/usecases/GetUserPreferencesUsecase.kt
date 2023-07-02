package com.willor.lib_data.domain.usecases

import com.willor.lib_data.data.local.prefs.UserPreferences
import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.DataResourceState
import kotlinx.coroutines.flow.Flow

class GetUserPreferencesUsecase(
    private val repo: Repo
) {

    operator fun invoke(): Flow<DataResourceState<UserPreferences>> {
        return repo.getUserPreferences()
    }
}