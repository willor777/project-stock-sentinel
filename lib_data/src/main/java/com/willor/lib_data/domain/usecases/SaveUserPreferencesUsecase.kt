package com.willor.lib_data.domain.usecases

import com.willor.lib_data.data.local.prefs.UserPreferences
import com.willor.lib_data.domain.Repo

class SaveUserPreferencesUsecase(
    private val repo: Repo
) {

    suspend operator fun invoke(userPreferences: UserPreferences){
        repo.saveUserPreferences(userPreferences)
    }
}