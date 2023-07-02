package com.willor.lib_data.domain.dataobjs.responses.popular_wl_options_resp


import com.google.gson.annotations.SerializedName

data class PopularWatchlistOptions(
    @SerializedName("data")
    val `data`: List<String>,
    @SerializedName("lastUpdated")
    val lastUpdated: Long
)