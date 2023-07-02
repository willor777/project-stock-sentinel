package com.willor.lib_data.domain.dataobjs.responses.etf_quote_resp


import com.google.gson.annotations.SerializedName

data class EtfQuote(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("lastUpdated")
    val lastUpdated: Long
)