package com.willor.lib_data.domain.usecases

import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.authentication_resp.LoginResponse
import kotlinx.coroutines.flow.Flow

class LoginUsecase(
    private val repo: Repo
) {

    operator fun invoke(username: String, password: String): Flow<DataResourceState<LoginResponse>> {
        return repo.loginUser(username, password)
    }
}