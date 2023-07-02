package com.willor.lib_data.domain.dataobjs.responses.uoa_page_resp


import com.google.gson.annotations.SerializedName

data class UnusualOptionsPage(
    @SerializedName("currentPage")
    val currentPage: Int,
    @SerializedName("data")
    val `data`: List<UnusualOption>,
    @SerializedName("lastUpdatedEpochSeconds")
    val lastUpdatedEpochSeconds: Int,
    @SerializedName("pagesAvailable")
    val pagesAvailable: Int
)