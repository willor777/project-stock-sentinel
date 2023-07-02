package com.willor.lib_data.domain.dataobjs.responses.stock_competitors_resp


import com.google.gson.annotations.SerializedName

data class Competitor(
    @SerializedName("companyName")
    val companyName: String,
    @SerializedName("marketCap")
    val marketCap: Long,
    @SerializedName("marketCapAbbreviatedString")
    val marketCapAbbreviatedString: String,
    @SerializedName("pctChange")
    val pctChange: Double,
    @SerializedName("ticker")
    val ticker: String
)