package com.willor.sentinel_v2.presentation.dashboard.dash_components

import androidx.compose.runtime.Stable
import com.willor.lib_data.data.local.prefs.UserPreferences
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.major_futures_resp.MajorFutures
import com.willor.lib_data.domain.dataobjs.responses.major_indices_resp.MajorIndices
import com.willor.lib_data.domain.dataobjs.responses.popular_wl_options_resp.PopularWatchlistOptions
import com.willor.lib_data.domain.dataobjs.responses.popular_wl_resp.PopularWatchlist

@Stable
data class DashboardUiState(
    val majorFutures: DataResourceState<MajorFutures?> = DataResourceState.Loading(),
    val majorIndices: DataResourceState<MajorIndices?> = DataResourceState.Loading(),
    val popularWatchlistOptions: DataResourceState<PopularWatchlistOptions?> = DataResourceState.Loading(),
    val popularWatchlistDisplayed: DataResourceState<PopularWatchlist?> = DataResourceState.Loading(),
    val userPrefs: DataResourceState<UserPreferences> = DataResourceState.Loading()
)