package com.willor.lib_data.domain.dataobjs.responses.stock_quote_resp


import com.google.gson.annotations.SerializedName

data class StockQuote(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("lastUpdated")
    val lastUpdated: Long
)