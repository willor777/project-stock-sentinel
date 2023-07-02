package com.willor.lib_data.domain.dataobjs.responses.chart_resp


import com.google.gson.annotations.SerializedName

data class StockChart(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("lastUpdated")
    val lastUpdated: Long
)