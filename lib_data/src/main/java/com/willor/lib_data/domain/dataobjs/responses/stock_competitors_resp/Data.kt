package com.willor.lib_data.domain.dataobjs.responses.stock_competitors_resp


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("competitorsList")
    val competitorList: List<Competitor>
)