package com.willor.lib_data.domain.dataobjs.responses.authentication_resp


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("expiresAt")
    val expiresAt: Long,
    @SerializedName("lastUpdated")
    val lastUpdated: Long,
    @SerializedName("token")
    val token: String
)