package com.willor.sentinel_v2.presentation.testing

import androidx.compose.runtime.Stable
import com.willor.lib_data.data.local.prefs.UserPreferences
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.etf_quote_resp.EtfQuote
import com.willor.lib_data.domain.dataobjs.responses.major_futures_resp.MajorFutures
import com.willor.lib_data.domain.dataobjs.responses.major_indices_resp.MajorIndices
import com.willor.lib_data.domain.dataobjs.responses.options_overview_resp.OptionsOverview
import com.willor.lib_data.domain.dataobjs.responses.popular_wl_options_resp.PopularWatchlistOptions
import com.willor.lib_data.domain.dataobjs.responses.popular_wl_resp.PopularWatchlist
import com.willor.lib_data.domain.dataobjs.responses.stock_competitors_resp.StockCompetitors
import com.willor.lib_data.domain.dataobjs.responses.stock_quote_resp.StockQuote
import com.willor.lib_data.domain.dataobjs.responses.stock_snr_levels_resp.StockSnrLevels
import com.willor.sentinel_v2.presentation.common.Screens

sealed class UiState{

    @Stable data class DashboardScreenState(
        val screen: Screens = Screens.Dashboard,
        val userPrefs: DataResourceState<UserPreferences> = DataResourceState.Loading(),
        val majorFutures: DataResourceState<MajorFutures?> = DataResourceState.Loading(),
        val majorIndices: DataResourceState<MajorIndices?> = DataResourceState.Loading(),
        val popularWatchlistOptions: DataResourceState<PopularWatchlistOptions?> = DataResourceState.Loading(),
        val popularWatchlistDisplayed: DataResourceState<PopularWatchlist?> = DataResourceState.Loading(),
        ): UiState()

    @Stable data class QuoteScreenState(
        val screen: Screens = Screens.Quotes,
        val userPrefs: DataResourceState<UserPreferences> = DataResourceState.Loading(),
        val currentTicker: String = "",
        val currentSearchText: String = "",
        val currentSearchResults: List<List<String>> = listOf(),
        val stockQuote: DataResourceState<StockQuote?> = DataResourceState.Loading(),
        val etfQuote: DataResourceState<EtfQuote?> = DataResourceState.Loading(),
        val optionsOverview: DataResourceState<OptionsOverview?> = DataResourceState.Loading(),
        val competitors: DataResourceState<StockCompetitors?> = DataResourceState.Loading(),
        val snrLevels: DataResourceState<StockSnrLevels?> = DataResourceState.Loading()
    )
}
