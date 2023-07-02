package com.willor.lib_data.domain.dataobjs.responses.major_indices_resp


import com.google.gson.annotations.SerializedName

data class MajorIndices(
    @SerializedName("data")
    val `data`: List<Index>,
    @SerializedName("lastUpdated")
    val lastUpdated: Long
)