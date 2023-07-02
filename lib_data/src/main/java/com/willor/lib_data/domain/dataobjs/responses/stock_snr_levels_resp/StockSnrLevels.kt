package com.willor.lib_data.domain.dataobjs.responses.stock_snr_levels_resp


import com.google.gson.annotations.SerializedName

data class StockSnrLevels(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("lastUpdated")
    val lastUpdated: Long
)