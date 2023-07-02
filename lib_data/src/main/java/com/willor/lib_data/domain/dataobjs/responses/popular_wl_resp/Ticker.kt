package com.willor.lib_data.domain.dataobjs.responses.popular_wl_resp


import com.google.gson.annotations.SerializedName

data class Ticker(
    @SerializedName("changeDollar")
    val changeDollar: Double,
    @SerializedName("changePercent")
    val changePercent: Double,
    @SerializedName("companyName")
    val companyName: String,
    @SerializedName("lastPrice")
    val lastPrice: Double,
    @SerializedName("marketCap")
    val marketCap: Long,
    @SerializedName("ticker")
    val ticker: String,
    @SerializedName("volume")
    val volume: Int,
    @SerializedName("volumeThirtyDayAvg")
    val volumeThirtyDayAvg: Int,
    @SerializedName("marketCapAbbreviatedString")
    val marketCapAbbreviatedString: String,
)