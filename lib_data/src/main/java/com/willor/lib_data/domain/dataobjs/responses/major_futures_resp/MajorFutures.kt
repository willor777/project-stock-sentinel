package com.willor.lib_data.domain.dataobjs.responses.major_futures_resp


import com.google.gson.annotations.SerializedName

data class MajorFutures(
    @SerializedName("data")
    val `data`: List<Future>,
    @SerializedName("lastUpdated")
    val lastUpdated: Long
)