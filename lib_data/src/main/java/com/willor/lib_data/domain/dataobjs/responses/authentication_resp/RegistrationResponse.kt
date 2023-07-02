package com.willor.lib_data.domain.dataobjs.responses.authentication_resp


import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("success")
    val success: Boolean
)