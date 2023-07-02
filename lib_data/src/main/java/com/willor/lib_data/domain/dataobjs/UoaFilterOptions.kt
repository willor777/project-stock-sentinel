package com.willor.lib_data.domain.dataobjs

enum class UoaFilterOptions(val key: String) {
    OTM_Percentage(key = "po"),
    Strike(key = "s"),
    Type(key = "t"),
    Expiry("expiry"),
    Last_Price("op"),
    Volume("v"),
    Open_Int("oi"),
    Volume_OI_Ratio("vol/oi"),
    Implied_Volatility("iv")
}