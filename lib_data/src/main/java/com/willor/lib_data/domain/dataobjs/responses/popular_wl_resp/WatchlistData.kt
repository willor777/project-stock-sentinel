package com.willor.lib_data.domain.dataobjs.responses.popular_wl_resp


import com.google.gson.annotations.SerializedName

data class WatchlistData(
    @SerializedName("name")
    val name: String,
    @SerializedName("tickers")
    val tickers: List<Ticker>
)