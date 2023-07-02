package com.willor.lib_data.domain.dataobjs.responses.popular_wl_search_resp


import com.google.gson.annotations.SerializedName

data class PopularWatchlistSearch(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("lastUpdated")
    val lastUpdated: Long
)