package com.willor.lib_data.domain.dataobjs.responses.stock_snr_levels_resp


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("approxPrice")
    val approxPrice: Double,
    @SerializedName("fibonacci38Pct")
    val fibonacci38Pct: Double,
    @SerializedName("fibonacci50Pct")
    val fibonacci50Pct: Double,
    @SerializedName("fibonacci62Pct")
    val fibonacci62Pct: Double,
    @SerializedName("fiftyTwoWeekHigh")
    val fiftyTwoWeekHigh: Double,
    @SerializedName("fiftyTwoWeekLow")
    val fiftyTwoWeekLow: Double,
    @SerializedName("r1")
    val r1: Double,
    @SerializedName("r2")
    val r2: Double,
    @SerializedName("r3")
    val r3: Double,
    @SerializedName("s1")
    val s1: Double,
    @SerializedName("s2")
    val s2: Double,
    @SerializedName("s3")
    val s3: Double,
    @SerializedName("ticker")
    val ticker: String
)