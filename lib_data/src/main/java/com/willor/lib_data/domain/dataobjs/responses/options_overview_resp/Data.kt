package com.willor.lib_data.domain.dataobjs.responses.options_overview_resp


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("historicalVolatilityPercentage")
    val historicalVolatilityPercentage: Double,
    @SerializedName("impVol")
    val impVol: Double,
    @SerializedName("impVolChangeToday")
    val impVolChangeToday: Double,
    @SerializedName("ivHighDate")
    val ivHighDate: String,
    @SerializedName("ivHighLastYear")
    val ivHighLastYear: Double,
    @SerializedName("ivLowDate")
    val ivLowDate: String,
    @SerializedName("ivLowLastYear")
    val ivLowLastYear: Double,
    @SerializedName("ivPercentile")
    val ivPercentile: Double,
    @SerializedName("ivRank")
    val ivRank: Double,
    @SerializedName("openInterestThirtyDay")
    val openInterestThirtyDay: Int,
    @SerializedName("openInterestToday")
    val openInterestToday: Int,
    @SerializedName("optionVolumeAvgThirtyDay")
    val optionVolumeAvgThirtyDay: Int,
    @SerializedName("optionVolumeToday")
    val optionVolumeToday: Int,
    @SerializedName("putCallOpenInterestRatio")
    val putCallOpenInterestRatio: Double,
    @SerializedName("putCallVolumeRatio")
    val putCallVolumeRatio: Double,
    @SerializedName("ticker")
    val ticker: String
)