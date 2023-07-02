package com.willor.lib_data.domain.dataobjs.responses.etf_quote_resp


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("askPrice")
    val askPrice: Double,
    @SerializedName("askSize")
    val askSize: Int,
    @SerializedName("avgVolume")
    val avgVolume: Int,
    @SerializedName("betaFiveYearMonthly")
    val betaFiveYearMonthly: Double,
    @SerializedName("bidPrice")
    val bidPrice: Double,
    @SerializedName("bidSize")
    val bidSize: Int,
    @SerializedName("changeDollarRegMarket")
    val changeDollarRegMarket: Double,
    @SerializedName("changePctRegMarket")
    val changePctRegMarket: Double,
    @SerializedName("daysRangeHigh")
    val daysRangeHigh: Double,
    @SerializedName("daysRangeLow")
    val daysRangeLow: Double,
    @SerializedName("expenseRatioNetPercentage")
    val expenseRatioNetPercentage: Double,
    @SerializedName("fiftyTwoWeekRangeHigh")
    val fiftyTwoWeekRangeHigh: Double,
    @SerializedName("fiftyTwoWeekRangeLow")
    val fiftyTwoWeekRangeLow: Double,
    @SerializedName("inceptionDate")
    val inceptionDate: String,
    @SerializedName("lastPriceRegMarket")
    val lastPriceRegMarket: Double,
    @SerializedName("nav")
    val nav: Double,
    @SerializedName("netAssets")
    val netAssets: Long,
    @SerializedName("netAssetsAbbreviatedString")
    val netAssetsAbbreviatedString: String,
    @SerializedName("openPrice")
    val openPrice: Double,
    @SerializedName("peRatioTTM")
    val peRatioTTM: Double,
    @SerializedName("prepostChangeDollar")
    val prepostChangeDollar: Double,
    @SerializedName("prepostChangePct")
    val prepostChangePct: Double,
    @SerializedName("prepostPrice")
    val prepostPrice: Double,
    @SerializedName("prevClose")
    val prevClose: Double,
    @SerializedName("quoteTimeStamp")
    val quoteTimeStamp: Long,
    @SerializedName("ticker")
    val ticker: String,
    @SerializedName("volume")
    val volume: Int,
    @SerializedName("yearToDateTotalReturn")
    val yearToDateTotalReturn: Double,
    @SerializedName("yieldPercentage")
    val yieldPercentage: Double,
    @SerializedName("name")
    val name: String,

)