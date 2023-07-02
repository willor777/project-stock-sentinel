package com.willor.lib_data.domain.dataobjs.responses.uoa_page_resp


import com.google.gson.annotations.SerializedName

data class UnusualOption(
    @SerializedName("ask")
    val ask: Double,
    @SerializedName("bid")
    val bid: Double,
    @SerializedName("contractExpiry")
    val contractExpiry: String,
    @SerializedName("contractType")
    val contractType: String,
    @SerializedName("impVol")
    val impVol: Double,
    @SerializedName("lastPrice")
    val lastPrice: Double,
    @SerializedName("openInt")
    val openInt: Int,
    @SerializedName("otmPercentage")
    val otmPercentage: Double,
    @SerializedName("strikePrice")
    val strikePrice: Double,
    @SerializedName("ticker")
    val ticker: String,
    @SerializedName("volOiRatio")
    val volOiRatio: Double,
    @SerializedName("volume")
    val volume: Int
)