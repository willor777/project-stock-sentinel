package com.willor.lib_data.domain.usecases

import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.authentication_resp.RegistrationResponse
import kotlinx.coroutines.flow.Flow

class RegisterUsecase(
    private val repo: Repo
) {

    operator fun invoke(
        username: String, password: String
    ): Flow<DataResourceState<RegistrationResponse>> {
        return repo.registerNewUser(username, password)
    }
}