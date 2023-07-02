package com.willor.lib_data.domain.dataobjs.responses.major_indices_resp


import com.google.gson.annotations.SerializedName

data class Index(
    @SerializedName("changeDollar")
    val changeDollar: Double,
    @SerializedName("changePercent")
    val changePercent: Double,
    @SerializedName("lastPrice")
    val lastPrice: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("ticker")
    val ticker: String,
    @SerializedName("volume")
    val volume: Long
)