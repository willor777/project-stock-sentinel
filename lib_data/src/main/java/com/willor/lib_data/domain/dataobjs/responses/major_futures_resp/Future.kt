package com.willor.lib_data.domain.dataobjs.responses.major_futures_resp


import com.google.gson.annotations.SerializedName

data class Future(
    @SerializedName("changeDollar")
    val changeDollar: Double,
    @SerializedName("changePercent")
    val changePercent: Double,
    @SerializedName("lastPrice")
    val lastPrice: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("nameAndExpiration")
    val nameAndExpiration: String,
    @SerializedName("ticker")
    val ticker: String,
    @SerializedName("volumeAvgThirtyDay")
    val volumeAvgThirtyDay: Int,
    @SerializedName("volumeToday")
    val volumeToday: Int
)