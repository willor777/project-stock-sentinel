package com.willor.lib_data.domain.dataobjs.responses.popular_wl_search_resp


import com.google.gson.annotations.SerializedName
import com.willor.lib_data.domain.dataobjs.responses.popular_wl_resp.Ticker

data class Data(
    @SerializedName("name")
    val name: String,
    @SerializedName("tickers")
    val tickers: List<Ticker>
)