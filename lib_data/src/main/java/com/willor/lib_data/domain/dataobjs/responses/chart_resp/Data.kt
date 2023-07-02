package com.willor.lib_data.domain.dataobjs.responses.chart_resp


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("close")
    val close: List<Double>,
    @SerializedName("datetime")
    val datetime: List<String>,
    @SerializedName("high")
    val high: List<Double>,
    @SerializedName("interval")
    val interval: String,
    @SerializedName("lastIndex")
    val lastIndex: Int,
    @SerializedName("low")
    val low: List<Double>,
    @SerializedName("open")
    val `open`: List<Double>,
    @SerializedName("periodRange")
    val periodRange: String,
    @SerializedName("prepost")
    val prepost: Boolean,
    @SerializedName("size")
    val size: Int,
    @SerializedName("ticker")
    val ticker: String,
    @SerializedName("timestamp")
    val timestamp: List<Int>,
    @SerializedName("volume")
    val volume: List<Int>
)