package com.willor.lib_data.data.local.prefs

import com.github.willor777.stock_analysis_lib.analysis.Strategies
import com.github.willor777.stock_analysis_lib.common.CandleInterval
import com.willor.lib_data.domain.dataobjs.UoaFilterOptions
import com.willor.lib_data.utils.gson

data class UserPreferences(
    
    // UI 
    var sentinelWatchlist: List<String> = listOf(),
    var lastPopularWatchlistSelected: String = "GAINERS",
    var uoaSortAsc: Boolean = false,
    var uoaSortBy: UoaFilterOptions = UoaFilterOptions.Volume_OI_Ratio,

    // Authentication
    var username: String = "",
    var apiKey: String = "",
    var apiKeyExpiry: Long = 0,
    
    // Scanner
    var scannerStrategy: Strategies = Strategies.TEST_STRATEGY,
    var scannerCandleInterval: CandleInterval = CandleInterval.M1
){
    companion object{
        fun toJson(userPrefs: UserPreferences): String{
            return gson.toJson(userPrefs)
        }

        fun toUserPreferences(jsonString: String): UserPreferences{
            return gson.fromJson(jsonString, UserPreferences::class.java)
        }
    }
}


