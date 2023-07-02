package com.willor.lib_data.domain.dataobjs.responses.options_overview_resp


import com.google.gson.annotations.SerializedName

data class OptionsOverview(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("lastUpdated")
    val lastUpdated: Long
)