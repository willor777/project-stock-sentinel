package com.willor.lib_data.domain.dataobjs.responses.stock_competitors_resp


import com.google.gson.annotations.SerializedName

data class StockCompetitors(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("lastUpdated")
    val lastUpdated: Long
)

