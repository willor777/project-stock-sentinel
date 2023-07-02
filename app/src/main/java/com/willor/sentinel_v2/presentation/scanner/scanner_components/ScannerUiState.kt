package com.willor.sentinel_v2.presentation.scanner.scanner_components

import androidx.compose.runtime.Stable
import com.willor.lib_data.data.local.prefs.UserPreferences
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.entities.TriggerEntity
import com.willor.lib_data.domain.dataobjs.responses.stock_quote_resp.StockQuote
import com.willor.sentinel_v2.presentation.common.Screens


@Stable
data class ScannerUiState(
    val screen: Screens = Screens.Scanner,
    val userPrefs: DataResourceState<UserPreferences> = DataResourceState.Loading(),
    val triggers: List<TriggerEntity> = listOf(),
    val stockQuotes: Map<String, StockQuote> = mapOf(),
)
