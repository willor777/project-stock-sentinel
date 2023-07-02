package com.willor.lib_data.domain.dataobjs.responses.popular_wl_resp


import com.google.gson.annotations.SerializedName

data class PopularWatchlist(
    @SerializedName("data")
    val watchlistData: WatchlistData,
    @SerializedName("lastUpdated")
    val lastUpdated: Long
)